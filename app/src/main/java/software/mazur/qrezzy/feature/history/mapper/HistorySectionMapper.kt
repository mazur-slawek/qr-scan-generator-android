package software.mazur.qrezzy.feature.history.mapper

import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.model.QrHistoryType
import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.history.model.HistoryItemUi
import software.mazur.qrezzy.feature.history.model.HistorySectionUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun List<QrHistoryItem>.toHistorySections(): List<HistorySectionUi> {
    return groupBy { item -> item.createdAt.toDateHeader() }
        .map { (date, items) ->
            HistorySectionUi(
                date = date,
                items = items.map { item -> item.toHistoryItemUi() },
            )
        }
}

private fun QrHistoryItem.toHistoryItemUi(): HistoryItemUi {
    return HistoryItemUi(
        id = id,
        qrType = type.toQrType(),
        value = title,
        source = source,
        createdAt = createdAt,
    )
}

private fun QrHistoryType.toQrType(): QrType {
    return when (this) {
        QrHistoryType.TEXT    -> QrType.Text()
        QrHistoryType.URL     -> QrType.Url()
        QrHistoryType.WIFI    -> QrType.Wifi()
        QrHistoryType.CONTACT -> QrType.Contact()
        QrHistoryType.EMAIL   -> QrType.Email()
        QrHistoryType.PHONE   -> QrType.Phone()
    }
}

private fun Long.toDateHeader(): String {
    return SimpleDateFormat(
        HistoryDateFormats.SECTION_DATE,
        Locale.getDefault(),
    ).format(Date(this))
}

private object HistoryDateFormats {
    const val SECTION_DATE = "dd.MM.yyyy"
}