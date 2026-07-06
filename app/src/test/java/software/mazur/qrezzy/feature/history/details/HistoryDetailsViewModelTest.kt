package software.mazur.qrezzy.feature.history.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import software.mazur.qrezzy.domain.qr.usecase.DeleteQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.GetQrByIdUseCase
import software.mazur.qrezzy.domain.qr.usecase.ToggleQrFavoriteUseCase
import software.mazur.qrezzy.domain.qr.usecase.UpdateQrStyleUseCase
import software.mazur.qrezzy.feature.history.HistoryRoute
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should load qr by id`() = runTest {
        val qr = createQr(id = 1L, content = "QREZZY")
        val viewModel = createViewModel(qrRepository = FakeQrRepository(initialItems = listOf(qr)), historyId = 1L)
        collectUiState(viewModel)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(qr, viewModel.uiState.value.qr)
        assertFalse(viewModel.uiState.value.isMissing)
    }

    @Test
    fun `should set isMissing when qr does not exist`() = runTest {
        val viewModel = createViewModel(
            qrRepository = FakeQrRepository(),
            historyId = 404L
        )

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.qr)
        assertTrue(viewModel.uiState.value.isMissing)
    }

    @Test
    fun `should optimistically update favorite when favorite is toggled`() = runTest {
        val qr = createQr(id = 1L, isFavorite = false)
        val qrRepository = FakeQrRepository(initialItems = listOf(qr))
        val viewModel = createViewModel(qrRepository = qrRepository)

        advanceUntilIdle()
        viewModel.onFavoriteClick()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.qr!!.isFavorite)
        assertEquals(1L to true, qrRepository.updatedFavorite)
    }

    @Test
    fun `should rollback favorite when toggle favorite fails`() = runTest {
        val qr = createQr(id = 1L, isFavorite = false)
        val viewModel = createViewModel(
            qrRepository = FailingFavoriteQrRepository(initialItems = listOf(qr))
        )

        advanceUntilIdle()
        viewModel.onFavoriteClick()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.qr!!.isFavorite)
    }

    @Test
    fun `should delete qr and emit OnBack`() = runTest {
        val qr = createQr(id = 1L)
        val qrRepository = FakeQrRepository(initialItems = listOf(qr))
        val viewModel = createViewModel(qrRepository = qrRepository)

        advanceUntilIdle()

        viewModel.events.test {
            viewModel.onDeleteConfirmationDialogConfirm()
            advanceUntilIdle()

            assertEquals(HistoryDetailsUiEvent.OnBack, awaitItem())
            assertEquals(listOf(1L), qrRepository.deletedIds)
        }
    }

    @Test
    fun `should save style and emit QrStyleSaved`() = runTest {
        val qr = createQr(id = 1L)
        val qrRepository = FakeQrRepository(initialItems = listOf(qr))
        val viewModel = createViewModel(qrRepository = qrRepository)
        val expectedStyle = qr.style.copy(patternStyle = QrPatternStyle.DOTS)

        advanceUntilIdle()

        viewModel.events.test {
            viewModel.onCustomizeQrClick()
            viewModel.onPatternStyleSelected(QrPatternStyle.DOTS)
            advanceUntilIdle()

            viewModel.onSaveQrStyleClick()
            advanceUntilIdle()

            assertEquals(HistoryDetailsUiEvent.QrStyleSaved, awaitItem())
            assertEquals(expectedStyle, viewModel.uiState.value.qr!!.style)
            assertEquals(1L to expectedStyle, qrRepository.updatedStyle)
        }
    }

    @Test
    fun `should rollback style and emit QrStyleSaveFailed when save style fails`() = runTest {
        val originalStyle = QrStyle(patternStyle = QrPatternStyle.SQUARE)
        val qr = createQr(id = 1L, style = originalStyle)
        val viewModel = createViewModel(qrRepository = FailingStyleQrRepository(initialItems = listOf(qr)))

        advanceUntilIdle()

        viewModel.events.test {
            viewModel.onCustomizeQrClick()
            viewModel.onPatternStyleSelected(QrPatternStyle.DOTS)
            advanceUntilIdle()

            viewModel.onSaveQrStyleClick()
            advanceUntilIdle()

            assertEquals(HistoryDetailsUiEvent.QrStyleSaveFailed, awaitItem())
            assertEquals(originalStyle, viewModel.uiState.value.qr!!.style)
            assertFalse(viewModel.uiState.value.qrStyleEditor.isDialogVisible)
        }
    }

    private fun createViewModel(
        qrRepository: QrRepository = FakeQrRepository(initialItems = listOf(createQr(id = 1L))),
        historyId: Long = 1L
    ): HistoryDetailsViewModel {
        val qrBitmapGenerator = mockk<QrBitmapGenerator>()
        every { qrBitmapGenerator.generate(any(), any(), any()) } returns null

        return HistoryDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(HistoryRoute.Details.HISTORY_ID_ARG to historyId)
            ),
            getQrByIdUseCase = GetQrByIdUseCase(qrRepository),
            qrBitmapGenerator = qrBitmapGenerator,
            deleteQrItemsUseCase = DeleteQrItemsUseCase(qrRepository),
            toggleQrFavoriteUseCase = ToggleQrFavoriteUseCase(qrRepository),
            updateQrStyleUseCase = UpdateQrStyleUseCase(qrRepository)
        )
    }

    private class FailingFavoriteQrRepository(initialItems: List<Qr>) : FakeQrRepository(initialItems) {
        override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean): Unit =
            throw IllegalStateException("Favorite update failed")
    }

    private class FailingStyleQrRepository(initialItems: List<Qr>) : FakeQrRepository(initialItems) {
        override suspend fun updateStyle(id: Long, style: QrStyle): Unit = throw IllegalStateException("Style update failed")
    }

    private fun TestScope.collectUiState(viewModel: HistoryDetailsViewModel) {
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
    }
}
