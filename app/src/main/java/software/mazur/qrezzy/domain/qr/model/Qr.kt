package software.mazur.qrezzy.domain.qr.model

import software.mazur.qrezzy.domain.qr.model.style.QrStyle

data class Qr(
    val id: Long = 0,
    val type: QrType,
    val source: QrSource,
    val content: String,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val style: QrStyle = QrStyle()
)
