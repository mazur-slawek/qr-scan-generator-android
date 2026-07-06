package software.mazur.qrezzy.feature.history

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.usecase.DeleteQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.ObserveQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.ToggleQrFavoriteUseCase
import software.mazur.qrezzy.feature.history.model.HistoryTab
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should filter items by scanned tab`() = runTest {
        val scannedQr = createQr(id = 1L, source = QrSource.SCANNED, content = "scanned")
        val generatedQr = createQr(id = 2L, source = QrSource.GENERATED, content = "generated")
        val viewModel = createViewModel(
            repository = FakeQrRepository(initialItems = listOf(scannedQr, generatedQr))
        )
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onTabSelected(HistoryTab.SCANNED.id)
        advanceUntilIdle()

        assertEquals(listOf(scannedQr), viewModel.uiState.value.visibleRegularItems())
    }

    @Test
    fun `should filter items by generated tab`() = runTest {
        val scannedQr = createQr(id = 1L, source = QrSource.SCANNED, content = "scanned")
        val generatedQr = createQr(id = 2L, source = QrSource.GENERATED, content = "generated")
        val viewModel = createViewModel(
            repository = FakeQrRepository(initialItems = listOf(scannedQr, generatedQr))
        )
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onTabSelected(HistoryTab.GENERATED.id)
        advanceUntilIdle()

        assertEquals(listOf(generatedQr), viewModel.uiState.value.visibleRegularItems())
    }

    @Test
    fun `should filter items by search query`() = runTest {
        val matchingQr = createQr(id = 1L, content = "https://qrezzy.app")
        val notMatchingQr = createQr(id = 2L, content = "https://example.com")
        val viewModel = createViewModel(
            repository = FakeQrRepository(initialItems = listOf(matchingQr, notMatchingQr))
        )
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("qrezzy")
        advanceUntilIdle()

        assertEquals(listOf(matchingQr), viewModel.uiState.value.visibleRegularItems())
    }

    @Test
    fun `should toggle favorite`() = runTest {
        val qr = createQr(id = 1L, isFavorite = false)
        val repository = FakeQrRepository(initialItems = listOf(qr))
        val viewModel = createViewModel(repository = repository)
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onFavoriteClick(qr)
        advanceUntilIdle()

        assertEquals(1L to true, repository.updatedFavorite)
    }

    @Test
    fun `should select and unselect item in delete mode`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onEnterDeleteMode()
        advanceUntilIdle()

        viewModel.onHistoryItemClick(1L)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isDeleteModeEnabled)
        assertEquals(setOf(1L), viewModel.uiState.value.selectedItemIds)

        viewModel.onHistoryItemClick(1L)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.selectedItemIds.isEmpty())
    }

    @Test
    fun `should show delete confirmation when selected items exist`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onEnterDeleteMode()
        viewModel.onHistoryItemClick(1L)
        viewModel.onDeleteSelected()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isDeleteConfirmationVisible)
    }

    @Test
    fun `should delete selected items`() = runTest {
        val firstQr = createQr(id = 1L, content = "first")
        val secondQr = createQr(id = 2L, content = "second")
        val repository = FakeQrRepository(initialItems = listOf(firstQr, secondQr))
        val viewModel = createViewModel(repository = repository)
        collectUiState(viewModel)
        advanceUntilIdle()

        viewModel.onEnterDeleteMode()
        viewModel.onHistoryItemClick(1L)
        viewModel.onDeleteSelected()
        viewModel.onDeleteConfirmationDialogConfirm()
        advanceUntilIdle()

        assertEquals(listOf(1L), repository.deletedIds)
        assertFalse(viewModel.uiState.value.isDeleteModeEnabled)
        assertEquals(listOf(secondQr), viewModel.uiState.value.visibleRegularItems())
    }

    private fun createViewModel(repository: FakeQrRepository = FakeQrRepository()): HistoryViewModel = HistoryViewModel(
        observeQrItemsUseCase = ObserveQrItemsUseCase(repository),
        deleteQrItemsUseCase = DeleteQrItemsUseCase(repository),
        toggleQrFavoriteUseCase = ToggleQrFavoriteUseCase(repository)
    )

    private fun TestScope.collectUiState(viewModel: HistoryViewModel) {
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
    }

    private fun software.mazur.qrezzy.feature.history.model.HistoryUiState.visibleRegularItems(): List<Qr> =
        sections.flatMap { section -> section.items }
}
