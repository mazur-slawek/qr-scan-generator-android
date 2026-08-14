package software.mazur.qrezzy.feature.generator

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
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
import software.mazur.qrezzy.feature.generator.model.QrFieldError
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeCrashReporter
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.FakeTimeProvider
import software.mazur.qrezzy.test.createQr

class GeneratorViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
        )
    }

    private class FailingQrRepository : FakeQrRepository() {
        override suspend fun save(item: Qr): Long = throw IllegalStateException("Save failed")
    }
}
