package software.mazur.qrezzy.feature.history.model

import androidx.compose.runtime.Immutable
import software.mazur.qrezzy.domain.qr.model.Qr

@Immutable
data class HistoryUiState(
    val favoriteItems: List<Qr> = emptyList(),
    val sections: List<HistorySectionUi> = emptyList(),
    val isInitialLoading: Boolean = true,
    val isDeleteModeEnabled: Boolean = false,
    val selectedItemIds: Set<Long> = emptySet(),
    val isDeleteConfirmationVisible: Boolean = false,
) {
    val hasItems: Boolean
        get() = favoriteItems.isNotEmpty() || sections.isNotEmpty()
    val canDeleteSelected: Boolean
        get() = selectedItemIds.isNotEmpty()
}
