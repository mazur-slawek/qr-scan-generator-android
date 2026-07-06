package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class SetAutoSaveScansUseCaseTest {
    @Test
    fun `should update auto save scans setting`() = runTest {
        val repository = FakeAppSettingsRepository()
        val useCase = SetAutoSaveScansUseCase(repository)
        useCase(true)
        assertTrue(repository.observeSettings().first().autoSaveScans)
    }
}
