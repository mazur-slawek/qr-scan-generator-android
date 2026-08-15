package software.mazur.qrezzy.feature.generator

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import software.mazur.qrezzy.domain.qr.usecase.CreateGeneratedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.CanSaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.model.GeneratorUiState
import software.mazur.qrezzy.feature.generator.model.QrFieldError
import software.mazur.qrezzy.feature.generator.model.QrGenerationError
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.model.QrPreviewState
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeCrashReporter
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.FakeTimeProvider
import software.mazur.qrezzy.test.createQr

@OptIn(ExperimentalCoroutinesApi::class)
class GeneratorViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val createdViewModels = mutableListOf<GeneratorViewModel>()

    /**
     * GeneratorViewModel uruchamia długożyjące subskrypcje Flow w viewModelScope (observeQrPreview,
     * observeEditPreview). Anulowanie jest kooperacyjne i nie przerywa już trwającego, synchronicznego
     * wywołania na Dispatchers.Default (qrBitmapGenerator.generate) — dlatego trzeba cancelAndJoin(),
     * nie samo cancel(), żeby faktycznie odczekać zakończenie przed Dispatchers.resetMain() z reguły.
     */
    @After
    fun tearDown() = runBlocking {
        createdViewModels.forEach { it.viewModelScope.coroutineContext.job.cancelAndJoin() }
    }

    @Test
    fun `should update qr content when text input changes`() = runTest {
        val viewModel = createViewModel()

        viewModel.onFormEvent(QrInputField.Text, " Hello QREZZY ")

        assertEquals("Hello QREZZY", viewModel.uiState.value.qrContent)
    }

    @Test
    fun `should not save blank qr`() = runTest {
        val qrRepository = FakeQrRepository()
        val viewModel = createViewModel(qrRepository = qrRepository)

        viewModel.onFormEvent(QrInputField.Text, "   ")
        viewModel.saveQrCode()

        assertTrue(qrRepository.savedItems.isEmpty())
    }

    @Test
    fun `should save valid qr`() = runTest {
        val qrRepository = FakeQrRepository()
        val viewModel = createViewModel(qrRepository = qrRepository)

        viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
        viewModel.saveQrCode()

        assertEquals(1, qrRepository.savedItems.size)
        assertEquals("Hello QREZZY", qrRepository.savedItems.first().content)
        assertEquals(QrType.TEXT, qrRepository.savedItems.first().type)
        assertEquals(QrSource.GENERATED, qrRepository.savedItems.first().source)
    }

    @Test
    fun `should show history limit popup when limit is reached`() = runTest {
        val qrRepository = FakeQrRepository(
            initialItems = List(50) { index -> createQr(id = index.toLong()) }
        )
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_50)
        )
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            settingsRepository = settingsRepository
        )

        viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
        viewModel.saveQrCode()

        assertTrue(viewModel.uiState.value.isSaveBlockedByHistoryLimit)
        assertTrue(viewModel.uiState.value.showHistoryLimitReachedPopup)
        assertTrue(qrRepository.savedItems.isEmpty())
    }

    @Test
    fun `should set max length field error when input exceeds max length`() = runTest {
        val viewModel = createViewModel()
        val tooLong = "a".repeat(QrInputField.Text.maxLength + 1)

        viewModel.onFormEvent(QrInputField.Text, tooLong)

        assertEquals(
            QrFieldError.MaxLength(QrInputField.Text.maxLength),
            viewModel.uiState.value.fieldErrors[QrInputField.Text]
        )
    }

    @Test
    fun `should mark preview as generating immediately when content changes`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = createViewModel()

        viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")

        assertTrue(viewModel.uiState.value.qrPreview is QrPreviewState.Generating)

        settle(viewModel)
    }

    @Test
    fun `should block save immediately after content changes, before debounce completes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = createViewModel()

            viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")

            assertEquals(false, viewModel.uiState.value.canSave)

            settle(viewModel)
        }

    @Test
    fun `should set qrPreview to CannotEncode when content exceeds capacity for applied error correction`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = createViewModel()
            val tooLongForHighCorrection = "https://example.com/" + "a".repeat(1500)

            viewModel.onCustomizeQrClick()
            viewModel.onErrorCorrectionSelected(QrErrorCorrection.HIGH)
            viewModel.onApplyQrStyleClick()
            viewModel.onFormEvent(QrInputField.Url, tooLongForHighCorrection)

            val settled = settle(viewModel)

            assertEquals(QrPreviewState.Error(QrGenerationError.CannotEncode), settled.qrPreview)
            assertEquals(false, settled.canSave)
        }

    @Test
    fun `should mark qrPreview as generating immediately when style is applied`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = createViewModel()
        viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
        settle(viewModel)

        viewModel.onCustomizeQrClick()
        viewModel.onErrorCorrectionSelected(QrErrorCorrection.HIGH)
        settle(viewModel)

        viewModel.onApplyQrStyleClick()

        assertTrue(viewModel.uiState.value.qrPreview is QrPreviewState.Generating)

        settle(viewModel)
    }

    @Test
    fun `should not publish stale evaluation when content changes rapidly to blank`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = createViewModel()
        val tooLongForHighCorrection = "https://example.com/" + "a".repeat(1500)

        viewModel.onCustomizeQrClick()
        viewModel.onErrorCorrectionSelected(QrErrorCorrection.HIGH)
        viewModel.onApplyQrStyleClick()

        viewModel.onFormEvent(QrInputField.Url, tooLongForHighCorrection)
        viewModel.onFormEvent(QrInputField.Url, "")

        val settled = settle(viewModel)

        assertEquals(QrPreviewState.Idle, settled.qrPreview)
    }

    @Test
    fun `should generate edit preview immediately when customize dialog opens`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = createViewModel()
        val tooLongForHighCorrection = "https://example.com/" + "a".repeat(1500)
        viewModel.onFormEvent(QrInputField.Url, tooLongForHighCorrection)
        settle(viewModel)

        viewModel.onCustomizeQrClick()

        assertTrue(viewModel.uiState.value.editPreview is QrPreviewState.Generating)

        settle(viewModel)
    }

    @Test
    fun `should not update edit preview while customize dialog is closed`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = createViewModel()

        viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
        val settled = settle(viewModel)

        assertEquals(QrPreviewState.Idle, settled.editPreview)
    }

    @Test
    fun `should emit QrSaved when qr is saved`() = runTest {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
            viewModel.saveQrCode()

            assertEquals(GeneratorUiEvent.QrSaved, awaitItem())
        }
    }

    @Test
    fun `should emit QrSaveFailed when save fails`() = runTest {
        val qrRepository = FailingQrRepository()
        val viewModel = createViewModel(qrRepository = qrRepository)

        viewModel.events.test {
            viewModel.onFormEvent(QrInputField.Text, "Hello QREZZY")
            viewModel.saveQrCode()

            assertEquals(GeneratorUiEvent.QrSaveFailed, awaitItem())
        }
    }

    private fun createViewModel(
        qrRepository: QrRepository = FakeQrRepository(),
        settingsRepository: FakeAppSettingsRepository = FakeAppSettingsRepository()
    ): GeneratorViewModel {
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(
            qrRepository = qrRepository,
            observeAppSettingsUseCase = observeAppSettingsUseCase
        )

        return GeneratorViewModel(
            crashReporter = FakeCrashReporter(),
            createGeneratedQrUseCase = CreateGeneratedQrUseCase(FakeTimeProvider()),
            qrBitmapGenerator = QrBitmapGenerator(),
            saveQrUseCase = SaveQrUseCase(qrRepository),
            observeAppSettingsUseCase = observeAppSettingsUseCase,
            canSaveQrUseCase = CanSaveQrUseCase(getHistoryLimitStatusUseCase),
            getHistoryLimitStatusUseCase = getHistoryLimitStatusUseCase
        ).also { createdViewModels += it }
    }

    private class FailingQrRepository : FakeQrRepository() {
        override suspend fun save(item: Qr): Long = throw IllegalStateException("Save failed")
    }

    /**
     * Przesuwa czas debounce'a i odczekuje (przez realną suspensję, nie heurystykę schedulera),
     * aż qrPreview/editPreview przestaną być Generating — konieczne, bo generowanie przeskakuje
     * na realny Dispatchers.Default, którego advanceUntilIdle() samo w sobie nie potrafi zdrenować,
     * a niedokończona korutyna w viewModelScope po zakończeniu testu powoduje kaskadowe awarie
     * kolejnych testów (próba powrotu na Dispatchers.Main już po Dispatchers.resetMain()).
     */
    private suspend fun TestScope.settle(viewModel: GeneratorViewModel): GeneratorUiState {
        advanceUntilIdle()
        lateinit var settledState: GeneratorUiState
        viewModel.uiState.test {
            var state = awaitItem()
            while (state.qrPreview is QrPreviewState.Generating || state.editPreview is QrPreviewState.Generating) {
                state = awaitItem()
            }
            settledState = state
            cancelAndIgnoreRemainingEvents()
        }
        return settledState
    }
}
