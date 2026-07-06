package software.mazur.qrezzy.test

import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

fun createQr(
    id: Long = 1L,
    content: String = "QREZZY",
    type: QrType = QrType.TEXT,
    source: QrSource = QrSource.GENERATED,
    createdAt: Long = 1_000L,
    isFavorite: Boolean = false,
    style: QrStyle = QrStyle()
): Qr = Qr(id = id, content = content, type = type, source = source, createdAt = createdAt, isFavorite = isFavorite, style = style)
