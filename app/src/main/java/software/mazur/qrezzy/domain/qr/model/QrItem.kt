package software.mazur.qrezzy.domain.qr.model

data class QrItem(
    val id: Long = 0,
    val source: QrSource,
    val type: QrType,
    val title: String,
    val content: String,
    val payloadJson: String?,
    val createdAt: Long,
)