package software.mazur.qrezzy.feature.history.model

import androidx.compose.runtime.Immutable

@Immutable
data class HistorySectionUi(
    val date: String,
    val items: List<HistoryItemUi>
)