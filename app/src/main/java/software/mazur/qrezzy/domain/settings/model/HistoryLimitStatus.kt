package software.mazur.qrezzy.domain.settings.model

data class HistoryLimitStatus(val currentCount: Int, val historyLimit: HistoryLimit) {
    val maxItems: Int?
        get() = historyLimit.value
    val isUnlimited: Boolean
        get() = maxItems == null
    val isLimitReached: Boolean
        get() = maxItems?.let { currentCount >= it } ?: false
    val isLimitExceeded: Boolean
        get() = maxItems?.let { currentCount > it } ?: false
}