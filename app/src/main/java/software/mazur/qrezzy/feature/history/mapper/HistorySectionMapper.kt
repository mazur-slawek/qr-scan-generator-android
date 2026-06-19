package software.mazur.qrezzy.feature.history.mapper

import software.mazur.qrezzy.domain.qr.model.QrItem
import software.mazur.qrezzy.feature.history.model.HistoryItemUi
import software.mazur.qrezzy.feature.history.model.HistorySectionUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun List<QrItem>.toHistorySections(): List<HistorySectionUi> {
    return groupBy { item -> item.createdAt.toDateHeader() }
        .map { (date, items) -> HistorySectionUi(date = date, items = items.map { item -> item.toHistoryItemUi() }) }
}

private fun QrItem.toHistoryItemUi(): HistoryItemUi {
    return HistoryItemUi(id = id, qrType = type, value = title, source = source, createdAt = createdAt)
}

private fun Long.toDateHeader(): String {
    return SimpleDateFormat(HistoryDateFormats.SECTION_DATE, Locale.getDefault()).format(Date(this))
}

private object HistoryDateFormats {
    const val SECTION_DATE = "dd.MM.yyyy"
}