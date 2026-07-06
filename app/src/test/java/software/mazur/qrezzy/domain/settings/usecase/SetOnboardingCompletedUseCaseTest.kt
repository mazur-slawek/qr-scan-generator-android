package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import software.mazur.qrezzy.test.FakeAppSettingsRepository

class SetOnboardingCompletedUseCaseTest {
    @Test
    fun `should update onboarding completed setting`() = runTest {
        val repository = FakeAppSettingsRepository()
        val useCase = SetOnboardingCompletedUseCase(repository)
        useCase(true)
        assertTrue(repository.observeSettings().first().onboardingCompleted)
    }
}
