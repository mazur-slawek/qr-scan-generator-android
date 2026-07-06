package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class SetHistoryLimitUseCaseTest {
    @Test
    fun `should update history limit`() = runTest {
        val repository = FakeAppSettingsRepository()
        val useCase = SetHistoryLimitUseCase(repository)
        useCase(HistoryLimit.ITEMS_50)
        assertEquals(HistoryLimit.ITEMS_50, repository.observeSettings().first().historyLimit)
    }
}
