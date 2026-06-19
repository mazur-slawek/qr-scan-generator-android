package software.mazur.qrezzy.feature.history.model

import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType

data class HistoryItemUi(
    val id: Long,
    val qrType: QrType,
    val value: String,
    val source: QrSource,
    val createdAt: Long
)