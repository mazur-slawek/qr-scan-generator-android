package software.mazur.qrezzy.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

open class FakeAppSettingsRepository(initialSettings: AppSettings? = AppSettings()) : AppSettingsRepository {
    private val settings = MutableStateFlow(initialSettings ?: AppSettings())
    private var hasSettings = initialSettings != null

    override fun observeSettings(): Flow<AppSettings> = settings

    override suspend fun initializeSettingsIfNeeded(language: AppLanguage) {
        if (!hasSettings) {
            settings.value = AppSettings(language = language)
            hasSettings = true
        }
    }

    override suspend fun setOnboardingCompleted(value: Boolean) {
        settings.value = settings.value.copy(onboardingCompleted = value)
    }

    override suspend fun setLanguage(value: AppLanguage) {
        settings.value = settings.value.copy(language = value)
    }

    override suspend fun setTheme(value: AppTheme) {
        settings.value = settings.value.copy(theme = value)
    }

    override suspend fun setAutoSaveScans(value: Boolean) {
        settings.value = settings.value.copy(autoSaveScans = value)
    }

    override suspend fun setVibrationEnabled(value: Boolean) {
        settings.value = settings.value.copy(vibrationEnabled = value)
    }

    override suspend fun setHistoryLimit(value: HistoryLimit) {
        settings.value = settings.value.copy(historyLimit = value)
    }
}
