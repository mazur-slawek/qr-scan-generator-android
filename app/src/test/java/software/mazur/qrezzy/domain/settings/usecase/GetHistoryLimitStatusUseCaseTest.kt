package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class GetHistoryLimitStatusUseCaseTest {
    @Test
    fun `should return not reached when items count is below limit`() = runTest {
        val qrRepository = FakeQrRepository(initialItems = List(49) { index -> createQr(id = index.toLong()) })
        val settingsRepository = FakeAppSettingsRepository(initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_50))
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val useCase = GetHistoryLimitStatusUseCase(qrRepository, observeAppSettingsUseCase)
        val result = useCase()
        assertFalse(result.isLimitReached)
    }

    @Test
    fun `should return reached when items count equals limit`() = runTest {
        val qrRepository = FakeQrRepository(initialItems = List(50) { index -> createQr(id = index.toLong()) })
        val settingsRepository = FakeAppSettingsRepository(initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_50))
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val useCase = GetHistoryLimitStatusUseCase(qrRepository, observeAppSettingsUseCase)
        val result = useCase()
        assertTrue(result.isLimitReached)
    }
}
