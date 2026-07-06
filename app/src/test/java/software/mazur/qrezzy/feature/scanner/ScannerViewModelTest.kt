package software.mazur.qrezzy.feature.scanner

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import software.mazur.qrezzy.MainDispatcherRule
import software.mazur.qrezzy.core.common.vibration.VibrationService
import software.mazur.qrezzy.core.qr.decoder.QrImageDecoder
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.usecase.CreateScannedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.CanSaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.FakeTimeProvider
import software.mazur.qrezzy.test.createQr

class ScannerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should set mode to scanning when start scanning`() = runTest {
        val viewModel = createViewModel()
        viewModel.onStartScanning()
        assertEquals(ScannerUiState.Mode.Scanning, viewModel.uiState.value.mode)
    }

    @Test
    fun `should ignore blank qr content`() = runTest {
        val qrRepository = FakeQrRepository()
        val vibrationService = FakeVibrationService()
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            vibrationService = vibrationService
        )

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("   ")

        assertEquals(ScannerUiState.Mode.Scanning, viewModel.uiState.value.mode)
        assertNull(viewModel.uiState.value.detectedQr)
        assertTrue(qrRepository.savedItems.isEmpty())
        assertEquals(0, vibrationService.vibrationCount)
    }

    @Test
    fun `should set detected qr when qr is scanned`() = runTest {
        val viewModel = createViewModel()

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")
        val detectedQr = viewModel.uiState.value.detectedQr

        assertEquals(ScannerUiState.Mode.Idle, viewModel.uiState.value.mode)
        assertEquals("https://qrezzy.app", detectedQr?.content)
        assertEquals(QrType.URL, detectedQr?.type)
        assertEquals(QrSource.SCANNED, detectedQr?.source)
    }

    @Test
    fun `should auto save scanned qr when auto save is enabled`() = runTest {
        val qrRepository = FakeQrRepository()
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(autoSaveScans = true)
        )
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            settingsRepository = settingsRepository
        )

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")

        assertEquals(1, qrRepository.savedItems.size)
        assertEquals("https://qrezzy.app", qrRepository.savedItems.first().content)
    }

    @Test
    fun `should not auto save scanned qr when history limit is reached`() = runTest {
        val qrRepository = FakeQrRepository(
            initialItems = List(50) { index -> createQr(id = index.toLong()) }
        )
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(
                autoSaveScans = true,
                historyLimit = HistoryLimit.ITEMS_50
            )
        )
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            settingsRepository = settingsRepository
        )

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")

        assertTrue(qrRepository.savedItems.isEmpty())
        assertTrue(viewModel.uiState.value.isSaveBlockedByHistoryLimit)
        assertTrue(viewModel.uiState.value.showHistoryLimitReachedPopup)
    }

    @Test
    fun `should vibrate when setting is enabled`() = runTest {
        val vibrationService = FakeVibrationService()
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(vibrationEnabled = true)
        )
        val viewModel = createViewModel(
            settingsRepository = settingsRepository,
            vibrationService = vibrationService
        )

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")

        assertEquals(1, vibrationService.vibrationCount)
    }

    @Test
    fun `should not vibrate when setting is disabled`() = runTest {
        val vibrationService = FakeVibrationService()
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(vibrationEnabled = false)
        )
        val viewModel = createViewModel(
            settingsRepository = settingsRepository,
            vibrationService = vibrationService
        )

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")

        assertEquals(0, vibrationService.vibrationCount)
    }

    private fun createViewModel(
        qrRepository: FakeQrRepository = FakeQrRepository(),
        settingsRepository: FakeAppSettingsRepository = FakeAppSettingsRepository(),
        vibrationService: FakeVibrationService = FakeVibrationService()
    ): ScannerViewModel {
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(
            qrRepository = qrRepository,
            observeAppSettingsUseCase = observeAppSettingsUseCase
        )

        return ScannerViewModel(
            createScannedQrUseCase = CreateScannedQrUseCase(FakeTimeProvider()),
            saveQrUseCase = SaveQrUseCase(qrRepository),
            qrImageDecoder = mockk<QrImageDecoder>(relaxed = true),
            observeAppSettingsUseCase = observeAppSettingsUseCase,
            canSaveQrUseCase = CanSaveQrUseCase(getHistoryLimitStatusUseCase),
            vibrationService = vibrationService,
            getHistoryLimitStatusUseCase = getHistoryLimitStatusUseCase
        )
    }

    private class FakeVibrationService : VibrationService {
        var vibrationCount = 0
            private set

        override fun vibrateShort() {
            vibrationCount++
        }
    }
}
