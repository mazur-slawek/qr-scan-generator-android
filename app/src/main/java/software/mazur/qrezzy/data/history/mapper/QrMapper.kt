package software.mazur.qrezzy.data.history.mapper

import software.mazur.qrezzy.data.history.local.QrEntity
import software.mazur.qrezzy.domain.qr.model.QrItem

fun QrEntity.toDomain(): QrItem {
    return QrItem(
        id = id,
        source = source,
        type = type,
        title = title,
        content = content,
        payloadJson = payloadJson,
        createdAt = createdAt
    )
}

fun QrItem.toEntity(): QrEntity {
    return QrEntity(
        id = id,
        source = source,
        type = type,
        title = title,
        content = content,
        payloadJson = payloadJson,
        createdAt = createdAt
    )
}