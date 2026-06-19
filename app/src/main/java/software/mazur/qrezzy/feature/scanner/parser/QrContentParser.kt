package software.mazur.qrezzy.feature.scanner.parser

import software.mazur.qrezzy.domain.qr.model.QrType

object QrContentParser {
    fun parse(content: String): ParserQrContent {
        val trimmed = content.trim()

        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") ->
                ParserQrContent(type = QrType.URL, title = trimmed)

            trimmed.startsWith("mailto:")                                   ->
                ParserQrContent(type = QrType.EMAIL, title = trimmed.removePrefix("mailto:"))

            trimmed.startsWith("tel:")                                      ->
                ParserQrContent(type = QrType.PHONE, title = trimmed.removePrefix("tel:"))

            trimmed.startsWith("WIFI:", ignoreCase = true)                  ->
                ParserQrContent(type = QrType.WIFI, title = "WiFi network")

            trimmed.startsWith("BEGIN:VCARD", ignoreCase = true)            ->
                ParserQrContent(type = QrType.CONTACT, title = "Contact")

            else                                                            ->
                ParserQrContent(type = QrType.TEXT, title = trimmed.take(MAX_TITLE_LENGTH))
        }
    }

    private const val MAX_TITLE_LENGTH = 50
}