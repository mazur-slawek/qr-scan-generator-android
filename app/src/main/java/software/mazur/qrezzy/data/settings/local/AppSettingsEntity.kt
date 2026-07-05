package software.mazur.qrezzy.data.settings.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

@Entity(tableName = AppSettingsEntity.TABLE_NAME)
data class AppSettingsEntity(
    @PrimaryKey val id: Int = DEFAULT_ID,
    val onboardingCompleted: Boolean = false,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.SYSTEM,
    val autoSaveScans: Boolean = false,
    val vibrationEnabled: Boolean = true,
    val historyLimit: HistoryLimit = HistoryLimit.ITEMS_200
) {
    companion object {
        const val TABLE_NAME = "app_settings"
        const val DEFAULT_ID = 1
    }
}
