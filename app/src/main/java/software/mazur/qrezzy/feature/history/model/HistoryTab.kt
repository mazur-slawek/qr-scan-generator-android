package software.mazur.qrezzy.feature.history.model

import software.mazur.qrezzy.domain.history.model.QrHistorySource

enum class HistoryTab(val id: Int, val source: QrHistorySource?) {
    ALL(id = 0, source = null),
    SCANNED(id = 1, source = QrHistorySource.SCANNED),
    GENERATED(id = 2, source = QrHistorySource.GENERATED);

    companion object {
        fun fromId(id: Int): HistoryTab {
            return entries.firstOrNull { tab -> tab.id == id } ?: ALL
        }
    }
}