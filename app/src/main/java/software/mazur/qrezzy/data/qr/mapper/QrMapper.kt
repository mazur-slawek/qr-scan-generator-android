package software.mazur.qrezzy.data.qr.mapper

import software.mazur.qrezzy.data.qr.local.QrEntity
import software.mazur.qrezzy.domain.qr.model.Qr

fun QrEntity.toDomain(): Qr =
    Qr(
        id = id,
        source = source,
        type = type,
        content = content,
        createdAt = createdAt,
    )

fun Qr.toEntity(): QrEntity =
    QrEntity(
        id = id,
        source = source,
        type = type,
        content = content,
        createdAt = createdAt,
    )
