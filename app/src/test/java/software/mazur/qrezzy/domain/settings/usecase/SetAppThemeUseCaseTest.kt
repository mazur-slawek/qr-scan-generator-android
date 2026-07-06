package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class SetAppThemeUseCaseTest {
    @Test
    fun `should update app theme`() = runTest {
        val repository = FakeAppSettingsRepository()
        val useCase = SetAppThemeUseCase(repository)
        useCase(AppTheme.DARK)
        assertEquals(AppTheme.DARK, repository.observeSettings().first().theme)
    }
}
