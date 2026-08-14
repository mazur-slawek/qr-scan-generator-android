package software.mazur.qrezzy.feature.scanner

import android.net.Uri
import app.cash.turbine.test
import io.mockk.coEvery
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
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.usecase.CreateScannedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.CanSaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState
import software.mazur.qrezzy.test.FakeAppSettingsRepository
import software.mazur.qrezzy.test.FakeCrashReporter
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

    @Test
    fun `should emit NoQrFoundInImage when no qr found in selected image`() = runTest {
        val qrImageDecoder = mockk<QrImageDecoder> {
            coEvery { decode(any()) } returns null
        }
        val viewModel = createViewModel(qrImageDecoder = qrImageDecoder)

        viewModel.events.test {
            viewModel.onImageSelected(mockk<Uri>(relaxed = true))
            assertEquals(ScannerUiEvent.NoQrFoundInImage, awaitItem())
        }
    }

    @Test
    fun `should emit QrSaveFailed and record exception when auto save fails`() = runTest {
        val qrRepository = FailingQrRepository()
        val settingsRepository = FakeAppSettingsRepository(
            initialSettings = AppSettings(autoSaveScans = true)
        )
        val crashReporter = FakeCrashReporter()
        val viewModel = createViewModel(
            qrRepository = qrRepository,
            settingsRepository = settingsRepository,
            crashReporter = crashReporter
        )

        viewModel.events.test {
            viewModel.onStartScanning()
            viewModel.onQrCodeScanned("https://qrezzy.app")

            assertEquals(ScannerUiEvent.QrSaveFailed, awaitItem())
        }
        assertEquals(1, crashReporter.exceptions.size)
    }

    @Test
    fun `should emit QrSaveFailed and record exception when manual save fails`() = runTest {
        val qrRepository = FailingQrRepository()
        val crashReporter = FakeCrashReporter()
        val viewModel = createViewModel(qrRepository = qrRepository, crashReporter = crashReporter)

        viewModel.onStartScanning()
        viewModel.onQrCodeScanned("https://qrezzy.app")

        viewModel.events.test {
            viewModel.onSaveScannedQrClick()
            assertEquals(ScannerUiEvent.QrSaveFailed, awaitItem())
        }
        assertEquals(1, crashReporter.exceptions.size)
    }

    private fun createViewModel(
        qrRepository: FakeQrRepository = FakeQrRepository(),
        settingsRepository: FakeAppSettingsRepository = FakeAppSettingsRepository(),
        vibrationService: FakeVibrationService = FakeVibrationService(),
        qrImageDecoder: QrImageDecoder = mockk<QrImageDecoder>(relaxed = true),
        crashReporter: FakeCrashReporter = FakeCrashReporter()
    ): ScannerViewModel {
        val observeAppSettingsUseCase = ObserveAppSettingsUseCase(settingsRepository)
        val getHistoryLimitStatusUseCase = GetHistoryLimitStatusUseCase(
            qrRepository = qrRepository,
            observeAppSettingsUseCase = observeAppSettingsUseCase
        )

        return ScannerViewModel(
            crashReporter = crashReporter,
            createScannedQrUseCase = CreateScannedQrUseCase(FakeTimeProvider()),
            saveQrUseCase = SaveQrUseCase(qrRepository),
            qrImageDecoder = qrImageDecoder,
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

    private class FailingQrRepository : FakeQrRepository() {
        override suspend fun save(item: Qr): Long = throw IllegalStateException("Save failed")
    }
}
