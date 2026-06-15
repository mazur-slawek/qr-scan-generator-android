package software.mazur.qrezzy.data.history.mapper

import software.mazur.qrezzy.data.history.local.QrHistoryEntity
import software.mazur.qrezzy.domain.history.model.QrHistoryItem

fun QrHistoryEntity.toDomain(): QrHistoryItem {
    return QrHistoryItem(
        id = id,
        source = source,
        type = type,
        title = title,
        content = content,
        payloadJson = payloadJson,
        createdAt = createdAt,
        isFavorite = isFavorite,
    )
}

fun QrHistoryItem.toEntity(): QrHistoryEntity {
    return QrHistoryEntity(
        id = id,
        source = source,
        type = type,
        title = title,
        content = content,
        payloadJson = payloadJson,
        createdAt = createdAt,
        isFavorite = isFavorite,
    )
}