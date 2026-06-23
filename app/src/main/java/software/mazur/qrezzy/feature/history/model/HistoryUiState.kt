package software.mazur.qrezzy.feature.history.model

import androidx.compose.runtime.Immutable

@Immutable
data class HistoryUiState(
    val sections: List<HistorySectionUi> = emptyList(),
    val isInitialLoading: Boolean = true,
    val isDeleteModeEnabled: Boolean = false,
    val selectedItemIds: Set<Long> = emptySet(),
    val isDeleteConfirmationVisible: Boolean = false,
) {
    val canDeleteSelected: Boolean
        get() = selectedItemIds.isNotEmpty()
}
