package software.mazur.qrezzy.feature.scanner.parser

import software.mazur.qrezzy.domain.history.model.QrHistoryType

data class ParserQrContent(
    val type: QrHistoryType,
    val title: String,
)