package software.mazur.qrezzy.domain.settings.repository

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

interface AppSettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun initializeSettingsIfNeeded(language: AppLanguage)
    suspend fun setOnboardingCompleted(value: Boolean)
    suspend fun setLanguage(value: AppLanguage)
    suspend fun setTheme(value: AppTheme)
    suspend fun setAutoSaveScans(value: Boolean)
    suspend fun setVibrationEnabled(value: Boolean)
    suspend fun setHistoryLimit(value: HistoryLimit)
}
