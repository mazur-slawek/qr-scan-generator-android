package software.mazur.qrezzy.data.qr.mapper

import software.mazur.qrezzy.data.qr.local.QrEntity
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

fun QrEntity.toDomain(): Qr = Qr(
    id = id,
    source = source,
    type = type,
    content = content,
    createdAt = createdAt,
    isFavorite = isFavorite,
    style = QrStyle(
        qrColor = qrColor,
        backgroundColor = backgroundColor,
        patternStyle = patternStyle,
        errorCorrection = errorCorrection
    )
)

fun Qr.toEntity(): QrEntity = QrEntity(
    id = id,
    source = source,
    type = type,
    content = content,
    createdAt = createdAt,
    isFavorite = isFavorite,
    qrColor = style.qrColor,
    backgroundColor = style.backgroundColor,
    patternStyle = style.patternStyle,
    errorCorrection = style.errorCorrection
)
