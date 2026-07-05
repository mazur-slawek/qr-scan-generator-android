package software.mazur.qrezzy.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.designsystem.extensions.label
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.usecase.DeleteQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.ObserveQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.ToggleQrFavoriteUseCase
import software.mazur.qrezzy.feature.history.mapper.toHistorySections
import software.mazur.qrezzy.feature.history.mapper.toTabItem
import software.mazur.qrezzy.feature.history.model.HistoryTab
import software.mazur.qrezzy.feature.history.model.HistoryUiState

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    observeQrItemsUseCase: ObserveQrItemsUseCase,
    private val deleteQrItemsUseCase: DeleteQrItemsUseCase,
    private val toggleQrFavoriteUseCase: ToggleQrFavoriteUseCase
) : ViewModel() {
    private val selectedTab = MutableStateFlow(HistoryTab.ALL)
    private val deleteModeState = MutableStateFlow(DeleteModeState())
    val searchQuery = MutableStateFlow("")
    val uiState =
        combine(
            observeQrItemsUseCase(),
            selectedTab,
            deleteModeState,
            searchQuery
        ) { historyItems, selectedTab, deleteModeState, searchQuery ->
            val visibleItems = historyItems
                .filterByTab(selectedTab)
                .filterBySearchQuery(searchQuery)
            val favoriteItems = visibleItems.filter { qr -> qr.isFavorite }
            val regularItems = visibleItems.filterNot { qr -> qr.isFavorite }

            HistoryUiState(
                favoriteItems = favoriteItems,
                sections = regularItems.toHistorySections(),
                isInitialLoading = false,
                isDeleteModeEnabled = deleteModeState.isEnabled,
                selectedItemIds = deleteModeState.selectedItemIds,
                isDeleteConfirmationVisible = deleteModeState.isDeleteConfirmationVisible
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )
    val selectedTabItem =
        selectedTab
            .map { tab -> tab.toTabItem() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HistoryTab.ALL.toTabItem()
            )

    fun onSearchQueryChange(query: String) {
        if (deleteModeState.value.isEnabled) return
        searchQuery.value = query
    }

    fun onClearSearchQuery() {
        searchQuery.value = ""
    }

    fun onTabSelected(tabId: Int) {
        if (deleteModeState.value.isEnabled) return
        selectedTab.value = HistoryTab.fromId(tabId)
    }

    fun onEnterDeleteMode() {
        deleteModeState.value = DeleteModeState(
            isEnabled = true,
            selectedItemIds = emptySet(),
            isDeleteConfirmationVisible = false
        )
    }

    fun onExitDeleteMode() {
        deleteModeState.value = DeleteModeState()
    }

    fun onHistoryItemClick(itemId: Long) {
        if (!deleteModeState.value.isEnabled) return

        deleteModeState.update { currentState ->
            val updatedSelectedIds =
                if (itemId in currentState.selectedItemIds) {
                    currentState.selectedItemIds - itemId
                } else {
                    currentState.selectedItemIds + itemId
                }

            currentState.copy(selectedItemIds = updatedSelectedIds)
        }
    }

    fun onDeleteSelected() {
        if (deleteModeState.value.selectedItemIds.isEmpty()) return

        deleteModeState.update { state ->
            state.copy(isDeleteConfirmationVisible = true)
        }
    }

    fun onDeleteConfirmationDialogDismiss() {
        deleteModeState.update { state ->
            state.copy(isDeleteConfirmationVisible = false)
        }
    }

    fun onDeleteConfirmationDialogConfirm() {
        val selectedIds = deleteModeState.value.selectedItemIds
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            deleteQrItemsUseCase(ids = selectedIds.toList())
            onExitDeleteMode()
        }
    }

    fun onFavoriteClick(qr: Qr) {
        if (deleteModeState.value.isEnabled) return
        viewModelScope.launch {
            toggleQrFavoriteUseCase(id = qr.id, isFavorite = !qr.isFavorite)
        }
    }

    private fun List<Qr>.filterByTab(tab: HistoryTab): List<Qr> =
        tab.source?.let { source -> filter { item -> item.source == source } } ?: this

    private fun List<Qr>.filterBySearchQuery(query: String): List<Qr> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.length < SEARCH_QUERY_MIN_LENGTH) return this

        return filter { qr ->
            qr.label.contains(normalizedQuery, ignoreCase = true) ||
                qr.content.contains(normalizedQuery, ignoreCase = true)
        }
    }

    private data class DeleteModeState(
        val isEnabled: Boolean = false,
        val selectedItemIds: Set<Long> = emptySet(),
        val isDeleteConfirmationVisible: Boolean = false
    )

    private companion object {
        private const val SEARCH_QUERY_MIN_LENGTH = 2
    }
}
