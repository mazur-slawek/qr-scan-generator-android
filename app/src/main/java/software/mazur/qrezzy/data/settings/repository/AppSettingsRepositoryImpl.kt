package software.mazur.qrezzy.data.settings.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.mazur.qrezzy.data.database.dao.AppSettingsDao
import software.mazur.qrezzy.data.settings.local.AppSettingsEntity
import software.mazur.qrezzy.data.settings.mapper.toDomain
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class AppSettingsRepositoryImpl @Inject constructor(private val dao: AppSettingsDao) : AppSettingsRepository {
    override fun observeSettings(): Flow<AppSettings> = dao.observeSettings().map { entity -> entity?.toDomain() ?: AppSettings() }

    override suspend fun initializeSettingsIfNeeded(language: AppLanguage) {
        dao.insertIfMissing(AppSettingsEntity(language = language))
    }

    override suspend fun setOnboardingCompleted(value: Boolean) {
        ensureSettingsExist()
        dao.setOnboardingCompleted(value)
    }

    override suspend fun setLanguage(value: AppLanguage) {
        ensureSettingsExist()
        dao.setLanguage(value)
    }

    override suspend fun setTheme(value: AppTheme) {
        dao.insertIfMissing(AppSettingsEntity())
        dao.setTheme(value)
    }

    override suspend fun setAutoSaveScans(value: Boolean) {
        ensureSettingsExist()
        dao.setAutoSaveScans(value)
    }

    override suspend fun setVibrationEnabled(value: Boolean) {
        ensureSettingsExist()
        dao.setVibrationEnabled(value)
    }

    override suspend fun setHistoryLimit(value: HistoryLimit) {
        ensureSettingsExist()
        dao.setHistoryLimit(value)
    }

    private suspend fun ensureSettingsExist() {
        dao.insertIfMissing(AppSettingsEntity())
    }
}
