package software.mazur.qrezzy.feature.history.mapper

import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabItem
import software.mazur.qrezzy.feature.history.model.HistoryTab

val historyTabs: List<QrezzyTabItem> =
    HistoryTab.entries.map { tab -> tab.toTabItem() }

fun HistoryTab.toTabItem(): QrezzyTabItem {
    return QrezzyTabItem(
        key = id,
        titleResId = titleResId,
    )
}

private val HistoryTab.titleResId: Int
    get() = when (this) {
        HistoryTab.ALL       -> R.string.history_tab_all
        HistoryTab.SCANNED   -> R.string.history_tab_scanned
        HistoryTab.GENERATED -> R.string.history_tab_generated
    }