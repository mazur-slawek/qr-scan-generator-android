package software.mazur.qrezzy.feature.history.model

import software.mazur.qrezzy.domain.history.model.QrHistorySource
import software.mazur.qrezzy.feature.generator.model.QrType

data class HistoryItemUi(
    val id: Long,
    val qrType: QrType,
    val value: String,
    val source: QrHistorySource,
    val createdAt: Long
)