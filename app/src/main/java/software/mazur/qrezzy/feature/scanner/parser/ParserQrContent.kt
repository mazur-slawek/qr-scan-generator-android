package software.mazur.qrezzy.feature.scanner.parser

import software.mazur.qrezzy.domain.qr.model.QrType

data class ParserQrContent(
    val type: QrType,
    val title: String,
)