package software.mazur.qrezzy.feature.settings.model

import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

data class SettingsUiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.SYSTEM,
    val autoSaveScans: Boolean = false,
    val vibrationEnabled: Boolean = true,
    val historyLimit: HistoryLimit = HistoryLimit.ITEMS_200,
    val historyItemsCount: Int = 0,
    val latestHistoryItemCreatedAt: Long? = null,
    val showClearHistoryDialog: Boolean = false,
    val showHistoryLimitReachedPopup: Boolean = false,
)