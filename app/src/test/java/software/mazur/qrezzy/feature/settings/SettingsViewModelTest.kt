package software.mazur.qrezzy.feature.settings

import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.core.common.vibration.VibrationService
import software.mazur.qrezzy.core.localization.QrezzyLocaleManager
import software.mazur.qrezzy.domain.qr.usecase.ClearHistoryUseCase
import software.mazur.qrezzy.domain.qr.usecase.GetHistorySummaryUseCase
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppLanguageUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppThemeUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAutoSaveScansUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetHistoryLimitUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetVibrationEnabledUseCase
import software.mazur.qrezzy.feature.settings.model.SettingsUiEvent
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should save language and apply locale when language is selected`() = runTest {
        val settingsRepository = FakeAppSettingsRepository()
        val localeManager = mockk<QrezzyLocaleManager>(relaxed = true)
        val viewModel = createViewModel(
            settingsRepository = settingsRepository,
            localeManager = localeManager
        )
        collectUiState(viewModel)

        viewModel.onLanguageSelected(AppLanguage.POLISH)
        advanceUntilIdle()

        assertEquals(AppLanguage.POLISH, viewModel.uiState.value.language)
        verify { localeManager.applyLanguage(AppLanguage.POLISH) }
    }

    @Test
    fun `should save theme when theme is selected`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onThemeSelected(AppTheme.DARK)
        advanceUntilIdle()

        assertEquals(AppTheme.DARK, viewModel.uiState.value.theme)
    }

    @Test
    fun `should emit AutoSaveEnabled when auto save is enabled`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onAutoSaveScansChanged(true)
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.AutoSaveEnabled, awaitItem())
            assertTrue(viewModel.uiState.value.autoSaveScans)
        }
    }

    @Test
    fun `should emit AutoSaveDisabled when auto save is disabled`() = runTest {
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(autoSaveScans = true)
        )
        val viewModel = createViewModel(settingsRepository = settingsRepository)
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onAutoSaveScansChanged(false)
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.AutoSaveDisabled, awaitItem())
            assertFalse(viewModel.uiState.value.autoSaveScans)
        }
    }

    @Test
    fun `should emit VibrationEnabled and vibrate when vibration is enabled`() = runTest {
        val vibrationService = FakeVibrationService()
        val viewModel = createViewModel(vibrationService = vibrationService)
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onVibrationEnabledChanged(true)
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.VibrationEnabled, awaitItem())
            assertTrue(viewModel.uiState.value.vibrationEnabled)
            assertEquals(1, vibrationService.vibrationCount)
        }
    }

    @Test
    fun `should emit VibrationDisabled and not vibrate when vibration is disabled`() = runTest {
        val vibrationService = FakeVibrationService()
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(vibrationEnabled = true)
        )
        val viewModel = createViewModel(
            settingsRepository = settingsRepository,
            vibrationService = vibrationService
        )
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onVibrationEnabledChanged(false)
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.VibrationDisabled, awaitItem())
            assertFalse(viewModel.uiState.value.vibrationEnabled)
            assertEquals(0, vibrationService.vibrationCount)
        }
    }

    @Test
    fun `should show clear history dialog when clear history is clicked`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onClearHistoryClick()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.showClearHistoryDialog)
    }

    @Test
    fun `should clear history and emit HistoryCleared when clear history is confirmed`() = runTest {
        val qrRepository = FakeQrRepository(
            initialItems = listOf(createQr(id = 1L), createQr(id = 2L))
        )
        val viewModel = createViewModel(qrRepository = qrRepository)
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onClearHistoryClick()
            viewModel.onClearHistoryConfirmed()
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.HistoryCleared, awaitItem())
            assertTrue(qrRepository.deletedAll)
            assertFalse(viewModel.uiState.value.showClearHistoryDialog)
            assertEquals(0, viewModel.uiState.value.historyItemsCount)
        }
    }

    @Test
    fun `should show history limit popup when selected limit is reached`() = runTest {
        val qrRepository = FakeQrRepository(
            initialItems = List(50) { index -> createQr(id = index.toLong()) }
        )
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(historyLimit = HistoryLimit.ITEMS_200)
        )
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            settingsRepository = settingsRepository
        )
        collectUiState(viewModel)

        viewModel.events.test {
            viewModel.onHistoryLimitSelected(HistoryLimit.ITEMS_50)
            advanceUntilIdle()

            assertEquals(SettingsUiEvent.HistoryLimitChanged, awaitItem())
            assertEquals(HistoryLimit.ITEMS_50, viewModel.uiState.value.historyLimit)
            assertTrue(viewModel.uiState.value.showHistoryLimitReachedPopup)
        }
    }

    private fun createViewModel(
        qrRepository: FakeQrRepository = FakeQrRepository(),
        settingsRepository: FakeAppSettingsRepository = FakeAppSettingsRepository(),
        localeManager: QrezzyLocaleManager = mockk(relaxed = true),
        vibrationService: VibrationService = FakeVibrationService()
    ): SettingsViewModel {
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)

        return SettingsViewModel(
            observeAppSettingsUseCase = observeAppSettingsUseCase,
            setAppLanguageUseCase = SetAppLanguageUseCase(settingsRepository),
            setAppThemeUseCase = SetAppThemeUseCase(settingsRepository),
            setAutoSaveScansUseCase = SetAutoSaveScansUseCase(settingsRepository),
            setVibrationEnabledUseCase = SetVibrationEnabledUseCase(settingsRepository),
            setHistoryLimitUseCase = SetHistoryLimitUseCase(settingsRepository),
            getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(
                qrRepository = qrRepository,
                observeAppSettingsUseCase = observeAppSettingsUseCase
            ),
            getHistorySummaryUseCase = GetHistorySummaryUseCase(qrRepository),
            clearHistoryUseCase = ClearHistoryUseCase(qrRepository),
            localeManager = localeManager,
            vibrationService = vibrationService
        )
    }

    private fun TestScope.collectUiState(viewModel: SettingsViewModel) {
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
    }

    private class FakeVibrationService : VibrationService {
        var vibrationCount = 0
            private set

        override fun vibrateShort() {
            vibrationCount++
        }
    }
}
