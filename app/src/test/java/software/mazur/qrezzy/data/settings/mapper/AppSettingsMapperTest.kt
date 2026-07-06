package software.mazur.qrezzy.data.settings.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.data.settings.local.AppSettingsEntity
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

class AppSettingsMapperTest {
    @Test
    fun `should map app settings entity to domain`() {
        val entity = AppSettingsEntity(
            id = 1,
            onboardingCompleted = true,
            language = AppLanguage.UKRAINIAN,
            theme = AppTheme.DARK,
            autoSaveScans = true,
            vibrationEnabled = false,
            historyLimit = HistoryLimit.ITEMS_50
        )
        val result = entity.toDomain()
        assertEquals(
            AppSettings(
                onboardingCompleted = true,
                language = AppLanguage.UKRAINIAN,
                theme = AppTheme.DARK,
                autoSaveScans = true,
                vibrationEnabled = false,
                historyLimit = HistoryLimit.ITEMS_50
            ),
            result
        )
    }
}
