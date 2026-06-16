package software.mazur.qrezzy.feature.history.model

import androidx.compose.runtime.Immutable

@Immutable
data class HistoryUiState(
    val sections: List<HistorySectionUi> = emptyList(),
    val isInitialLoading: Boolean = true
)