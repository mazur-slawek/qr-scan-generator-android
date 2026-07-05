package software.mazur.qrezzy.feature.history.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.feature.history.model.HistorySectionUi

fun List<Qr>.toHistorySections(): List<HistorySectionUi> = groupBy { item -> item.createdAt.toDateHeader() }.map { (date, items) ->
    HistorySectionUi(date = date, items = items)
}

private fun Long.toDateHeader(): String = SimpleDateFormat(HistoryDateFormats.SECTION_DATE, Locale.getDefault()).format(Date(this))

private object HistoryDateFormats {
    const val SECTION_DATE = "dd.MM.yyyy"
}
