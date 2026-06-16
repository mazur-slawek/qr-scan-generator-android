package software.mazur.qrezzy.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.usecase.ObserveQrHistoryUseCase
import software.mazur.qrezzy.feature.history.mapper.toHistorySections
import software.mazur.qrezzy.feature.history.mapper.toTabItem
import software.mazur.qrezzy.feature.history.model.HistoryTab
import software.mazur.qrezzy.feature.history.model.HistoryUiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(observeQrHistoryUseCase: ObserveQrHistoryUseCase) : ViewModel() {
    private val selectedTab = MutableStateFlow(HistoryTab.ALL)
    val uiState = combine(observeQrHistoryUseCase(), selectedTab) { historyItems, selectedTab ->
        val visibleItems = historyItems.filterByTab(selectedTab)

        HistoryUiState(
            sections = visibleItems.toHistorySections(),
            isInitialLoading = false,
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
        selectedTab.value = HistoryTab.fromId(tabId)
    }

    private fun List<QrHistoryItem>.filterByTab(tab: HistoryTab): List<QrHistoryItem> {
        return tab.source?.let { source ->
            filter { item -> item.source == source }
        } ?: this
    }
}