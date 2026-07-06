package software.mazur.qrezzy.feature.onboarding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.domain.settings.usecase.SetOnboardingCompletedUseCase
import software.mazur.qrezzy.test.FakeAppSettingsRepository

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should set onboarding completed to true`() = runTest {
        val settingsRepository = FakeAppSettingsRepository()
        val viewModel = createViewModel(settingsRepository)

        viewModel.completeOnboarding(onCompleted = {})

        advanceUntilIdle()
        val settings = settingsRepository.observeSettings().first()
        assertTrue(settings.onboardingCompleted)
    }

    @Test
    fun `should call callback after onboarding is completed`() = runTest {
        val settingsRepository = FakeAppSettingsRepository()
        val viewModel = createViewModel(settingsRepository)
        var callbackCalled = false

        viewModel.completeOnboarding {
            callbackCalled = true
        }
        advanceUntilIdle()
        assertTrue(callbackCalled)
    }

    private fun createViewModel(settingsRepository: FakeAppSettingsRepository): OnboardingViewModel =
        OnboardingViewModel(setOnboardingCompletedUseCase = SetOnboardingCompletedUseCase(settingsRepository))
}
