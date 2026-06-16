package software.mazur.qrezzy.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.usecase.DeleteQrUseCase
import software.mazur.qrezzy.domain.history.usecase.ObserveQrHistoryUseCase
import software.mazur.qrezzy.feature.history.mapper.toHistorySections
import software.mazur.qrezzy.feature.history.mapper.toTabItem
import software.mazur.qrezzy.feature.history.model.HistoryTab
import software.mazur.qrezzy.feature.history.model.HistoryUiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observeQrHistoryUseCase: ObserveQrHistoryUseCase,
    private val deleteQrUseCase: DeleteQrUseCase,
) : ViewModel() {
    private val selectedTab = MutableStateFlow(HistoryTab.ALL)
    private val deleteModeState = MutableStateFlow(DeleteModeState())
    val uiState = combine(
        observeQrHistoryUseCase(),
        selectedTab,
        deleteModeState,
    ) { historyItems, selectedTab, deleteModeState ->
        val visibleItems = historyItems.filterByTab(selectedTab)

        HistoryUiState(
            sections = visibleItems.toHistorySections(),
            isInitialLoading = false,
            isDeleteModeEnabled = deleteModeState.isEnabled,
            selectedItemIds = deleteModeState.selectedItemIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )
    val selectedTabItem = selectedTab
        .map { tab -> tab.toTabItem() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryTab.ALL.toTabItem(),
        )

    fun onTabSelected(tabId: Int) {
        if (deleteModeState.value.isEnabled) return
        selectedTab.value = HistoryTab.fromId(tabId)
    }

    fun onEnterDeleteMode() {
        deleteModeState.value = DeleteModeState(isEnabled = true, selectedItemIds = emptySet())
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
            currentState.copy(
                selectedItemIds = updatedSelectedIds,
            )
        }
    }

    fun onDeleteSelected() {
        val selectedIds = deleteModeState.value.selectedItemIds
        if (selectedIds.isEmpty()) return
        viewModelScope.launch {
            deleteQrUseCase(ids = selectedIds.toList())
            onExitDeleteMode()
        }
    }

    private fun List<QrHistoryItem>.filterByTab(tab: HistoryTab): List<QrHistoryItem> {
        return tab.source?.let { source -> filter { item -> item.source == source } } ?: this
    }

    private data class DeleteModeState(
        val isEnabled: Boolean = false,
        val selectedItemIds: Set<Long> = emptySet(),
    )
}