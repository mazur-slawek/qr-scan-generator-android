package software.mazur.qrezzy.domain.history.model

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