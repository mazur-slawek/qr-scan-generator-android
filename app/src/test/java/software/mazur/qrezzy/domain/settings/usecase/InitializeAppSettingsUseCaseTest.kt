package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import java.util.Locale

class InitializeAppSettingsUseCaseTest {
    @Test
    fun `should initialize settings with provided system language`() = runTest {
        val repository = FakeAppSettingsRepository(null)
        val useCase = InitializeAppSettingsUseCase(repository)

        useCase(Locale.forLanguageTag("uk"))

        assertEquals(
            AppLanguage.UKRAINIAN,
            repository.observeSettings().first().language
        )
    }
}
