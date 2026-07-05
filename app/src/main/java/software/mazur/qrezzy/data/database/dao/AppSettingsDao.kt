package software.mazur.qrezzy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.data.settings.local.AppSettingsEntity
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    fun observeSettings(): Flow<AppSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfMissing(entity: AppSettingsEntity)

    @Query("UPDATE app_settings SET onboardingCompleted = :value WHERE id = 1")
    suspend fun setOnboardingCompleted(value: Boolean)

    @Query("UPDATE app_settings SET language = :value WHERE id = 1")
    suspend fun setLanguage(value: AppLanguage)

    @Query("UPDATE app_settings SET theme = :value WHERE id = 1")
    suspend fun setTheme(value: AppTheme)

    @Query("UPDATE app_settings SET autoSaveScans = :value WHERE id = 1")
    suspend fun setAutoSaveScans(value: Boolean)

    @Query("UPDATE app_settings SET vibrationEnabled = :value WHERE id = 1")
    suspend fun setVibrationEnabled(value: Boolean)

    @Query("UPDATE app_settings SET historyLimit = :value WHERE id = 1")
    suspend fun setHistoryLimit(value: HistoryLimit)
}