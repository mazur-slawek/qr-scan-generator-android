package software.mazur.qrezzy.feature.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import software.mazur.qrezzy.domain.settings.usecase.InitializeAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.test.FakeAppSettingsRepository

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should navigate to onboarding when onboarding is not completed`() = runTest {
        val viewModel = createViewModel(
            settingsRepository = FakeAppSettingsRepository(
                initialSettings = AppSettings(onboardingCompleted = false)
            )
        )
        var onboardingRequired = false
        var homeRequired = false

        viewModel.checkStartDestination(
            onOnboardingRequired = { onboardingRequired = true },
            onHomeRequired = { homeRequired = true }
        )
        advanceUntilIdle()

        assertTrue(onboardingRequired)
        assertEquals(false, homeRequired)
    }

    @Test
    fun `should navigate to home when onboarding is completed`() = runTest {
        val viewModel = createViewModel(
            settingsRepository = FakeAppSettingsRepository(
                initialSettings = AppSettings(onboardingCompleted = true)
            )
        )
        var onboardingRequired = false
        var homeRequired = false

        viewModel.checkStartDestination(
            onOnboardingRequired = { onboardingRequired = true },
            onHomeRequired = { homeRequired = true }
        )
        advanceUntilIdle()

        assertEquals(false, onboardingRequired)
        assertTrue(homeRequired)
    }

    @Test
    fun `should initialize settings before choosing start destination`() = runTest {
        val settingsRepository = TrackingAppSettingsRepository(
            initialSettings = AppSettings(onboardingCompleted = true)
        )
        val viewModel = createViewModel(settingsRepository = settingsRepository)

        viewModel.checkStartDestination(
            onOnboardingRequired = {
                settingsRepository.calls.add("onboarding")
            },
            onHomeRequired = {
                settingsRepository.calls.add("home")
            }
        )
        advanceUntilIdle()

        assertEquals(listOf("initialize", "observe", "home"), settingsRepository.calls)
    }

    private fun createViewModel(settingsRepository: AppSettingsRepository): SplashViewModel = SplashViewModel(
        initializeAppSettingsUseCase = InitializeAppSettingsUseCase(settingsRepository),
        observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
    )

    private class TrackingAppSettingsRepository(initialSettings: AppSettings) : FakeAppSettingsRepository(initialSettings) {
        val calls = mutableListOf<String>()

        override suspend fun initializeSettingsIfNeeded(language: software.mazur.qrezzy.domain.settings.model.AppLanguage) {
            calls.add("initialize")
            super.initializeSettingsIfNeeded(language)
        }

        override fun observeSettings(): kotlinx.coroutines.flow.Flow<AppSettings> {
            calls.add("observe")
            return super.observeSettings()
        }
    }
}
