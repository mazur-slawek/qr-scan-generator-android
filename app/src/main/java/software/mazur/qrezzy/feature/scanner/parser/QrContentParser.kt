package software.mazur.qrezzy.feature.scanner.parser

import software.mazur.qrezzy.domain.history.model.QrHistoryType

object QrContentParser {
    fun parse(content: String): ParserQrContent {
        val trimmed = content.trim()

        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") ->
                ParserQrContent(type = QrHistoryType.URL, title = trimmed)

            trimmed.startsWith("mailto:")                                   ->
                ParserQrContent(type = QrHistoryType.EMAIL, title = trimmed.removePrefix("mailto:"))

            trimmed.startsWith("tel:")                                      ->
                ParserQrContent(type = QrHistoryType.PHONE, title = trimmed.removePrefix("tel:"))

            trimmed.startsWith("WIFI:", ignoreCase = true)                  ->
                ParserQrContent(type = QrHistoryType.WIFI, title = "WiFi network")

            trimmed.startsWith("BEGIN:VCARD", ignoreCase = true)            ->
                ParserQrContent(type = QrHistoryType.CONTACT, title = "Contact")

            else                                                            ->
                ParserQrContent(type = QrHistoryType.TEXT, title = trimmed.take(MAX_TITLE_LENGTH))
        }
    }

    private const val MAX_TITLE_LENGTH = 50
}