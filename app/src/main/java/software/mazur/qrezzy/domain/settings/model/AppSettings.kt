package software.mazur.qrezzy.domain.settings.model

data class AppSettings(
    val onboardingCompleted: Boolean = false,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.SYSTEM,
    val autoSaveScans: Boolean = false,
    val vibrationEnabled: Boolean = true,
    val historyLimit: HistoryLimit = HistoryLimit.ITEMS_200,
)