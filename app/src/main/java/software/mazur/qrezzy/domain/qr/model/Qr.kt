package software.mazur.qrezzy.domain.qr.model

data class Qr(
    val id: Long = 0,
    val type: QrType,
    val source: QrSource,
    val content: String,
    val createdAt: Long,
)
