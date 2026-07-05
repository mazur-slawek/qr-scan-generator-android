package software.mazur.qrezzy.domain.settings.model

data class HistoryLimitStatus(val currentCount: Int, val historyLimit: HistoryLimit) {
    val maxItems: Int?
        get() = historyLimit.value
    val isLimitReached: Boolean
        get() = maxItems?.let { currentCount >= it } ?: false
}
