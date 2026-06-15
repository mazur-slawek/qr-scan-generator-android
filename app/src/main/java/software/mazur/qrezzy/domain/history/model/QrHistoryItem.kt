package software.mazur.qrezzy.domain.history.model

import software.mazur.qrezzy.data.history.local.QrHistorySource
import software.mazur.qrezzy.data.history.local.QrHistoryType

data class QrHistoryItem(
    val id: Long = 0,
    val source: QrHistorySource,
    val type: QrHistoryType,
    val title: String,
    val content: String,
    val payloadJson: String?,
    val createdAt: Long,
    val isFavorite: Boolean = false
)