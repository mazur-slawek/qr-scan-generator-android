package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class SetAppLanguageUseCaseTest {
    @Test
    fun `should update app language`() = runTest {
        val repository = FakeAppSettingsRepository()
        val useCase = SetAppLanguageUseCase(repository)
        useCase(AppLanguage.POLISH)
        assertEquals(AppLanguage.POLISH, repository.observeSettings().first().language)
    }
}
