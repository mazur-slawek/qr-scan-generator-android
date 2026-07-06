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

class CanSaveQrUseCaseTest {
    @Test
    fun `should return true when history limit is not reached`() = runTest {
        val qrRepository = FakeQrRepository(initialItems = List(49) { index -> createQr(id = index.toLong()) })
        val settingsRepository = FakeAppSettingsRepository(initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_50))
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(qrRepository, observeAppSettingsUseCase)
        val useCase = CanSaveQrUseCase(getHistoryLimitStatusUseCase)
        val result = useCase()
        assertTrue(result)
    }

    @Test
    fun `should return false when history limit is reached`() = runTest {
        val qrRepository = FakeQrRepository(initialItems = List(50) { index -> createQr(id = index.toLong()) })
        val settingsRepository = FakeAppSettingsRepository(initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_50))
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(qrRepository, observeAppSettingsUseCase)
        val useCase = CanSaveQrUseCase(getHistoryLimitStatusUseCase)
        val result = useCase()
        assertFalse(result)
    }

    @Test
    fun `should return true when history limit is unlimited`() = runTest {
        val qrRepository = FakeQrRepository(initialItems = List(500) { index -> createQr(id = index.toLong()) })
        val settingsRepository = FakeAppSettingsRepository(initialSettings = AppSettings(historyLimit = HistoryLimit.UNLIMITED))
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(qrRepository = qrRepository, observeAppSettingsUseCase)
        val useCase = CanSaveQrUseCase(getHistoryLimitStatusUseCase)
        val result = useCase()
        assertTrue(result)
    }
}
