package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class ObserveAppSettingsUseCaseTest {
    @Test
    fun `should observe app settings`() = runTest {
        val settings = AppSettings(
            language = AppLanguage.POLISH,
            theme = AppTheme.DARK,
            autoSaveScans = true,
            vibrationEnabled = true,
            historyLimit = HistoryLimit.ITEMS_50,
            onboardingCompleted = true
        )
        val repository = FakeAppSettingsRepository(initialSettings = settings)
        val useCase = ObserveAppSettingsUseCase(repository)
        val result = useCase().first()
        assertEquals(settings, result)
    }
}
