package software.mazur.qrezzy.feature.history.model

import software.mazur.qrezzy.domain.qr.model.QrSource

enum class HistoryTab(val id: Int, val source: QrSource?) {
    ALL(id = 0, source = null),
    SCANNED(id = 1, source = QrSource.SCANNED),
    GENERATED(id = 2, source = QrSource.GENERATED);

    companion object {
        fun fromId(id: Int): HistoryTab {
            return entries.firstOrNull { tab -> tab.id == id } ?: ALL
        }
    }
}