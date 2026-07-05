package software.mazur.qrezzy.feature.scanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.common.vibration.VibrationService
import software.mazur.qrezzy.core.qr.decoder.QrImageDecoder
import software.mazur.qrezzy.domain.qr.usecase.CreateScannedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.CanSaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val createScannedQrUseCase: CreateScannedQrUseCase,
    private val saveQrUseCase: SaveQrUseCase,
    private val qrImageDecoder: QrImageDecoder,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val canSaveQrUseCase: CanSaveQrUseCase,
    private val vibrationService: VibrationService,
    private val getHistoryLimitStatusUseCase: GetHistoryLimitStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<ScannerUiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            observeAppSettingsUseCase().collect { settings ->
                val limitStatus = getHistoryLimitStatusUseCase()

                _uiState.update { state ->
                    state.copy(
                        autoSaveScans = settings.autoSaveScans,
                        vibrationEnabled = settings.vibrationEnabled,
                        isSaveBlockedByHistoryLimit = limitStatus.isLimitReached,
                        showHistoryLimitReachedPopup = limitStatus.isLimitReached
                    )
                }
            }
        }
    }

    fun onScreenOpened() {
        viewModelScope.launch {
            val status = getHistoryLimitStatusUseCase()

            _uiState.update { state ->
                state.copy(
                    isSaveBlockedByHistoryLimit = status.isLimitReached,
                    showHistoryLimitReachedPopup = status.isLimitReached
                )
            }
        }
    }

    fun onStartScanning() {
        _uiState.update { state ->
            state.copy(mode = ScannerUiState.Mode.Scanning, isTorchEnabled = false)
        }
    }

    fun onStopScanning() {
        _uiState.update { state ->
            state.copy(mode = ScannerUiState.Mode.Idle, isTorchEnabled = false, detectedQr = null)
        }
    }

    fun onPermissionDenied() {
        _uiState.update { state ->
            state.copy(mode = ScannerUiState.Mode.PermissionDenied, isTorchEnabled = false)
        }
    }

    fun onPermissionRestored() {
        _uiState.update { state ->
            state.copy(mode = ScannerUiState.Mode.Idle)
        }
    }

    fun onTorchClick() {
        if (!_uiState.value.isScanning) return

        _uiState.update { state ->
            state.copy(isTorchEnabled = !state.isTorchEnabled)
        }
    }

    fun onQrCodeScanned(content: String) {
        if (!_uiState.value.isScanning) return
        handleDetectedQr(content)
    }

    fun onImageSelected(uri: Uri) {
        if (_uiState.value.detectedQr != null) return

        viewModelScope.launch {
            val content = withContext(Dispatchers.Default) {
                qrImageDecoder.decode(uri)
            }

            if (content.isNullOrBlank()) {
                _events.emit(ScannerUiEvent.ShowError("No QR code found in this image"))
                return@launch
            }

            handleDetectedQr(content)
        }
    }

    private fun handleDetectedQr(content: String) {
        val trimmedContent = content.trim()
        if (trimmedContent.isBlank()) return
        if (_uiState.value.detectedQr != null) return
        val detectedQr = createScannedQrUseCase(trimmedContent)

        _uiState.update { state ->
            state.copy(
                mode = ScannerUiState.Mode.Idle,
                isTorchEnabled = false,
                detectedQr = detectedQr
            )
        }

        viewModelScope.launch {
            val settings = observeAppSettingsUseCase().first()
            val canSave = canSaveQrUseCase()

            if (settings.vibrationEnabled) {
                vibrationService.vibrateShort()
            }

            if (!canSave) {
                _uiState.update { state ->
                    state.copy(
                        isSaveBlockedByHistoryLimit = true,
                        showHistoryLimitReachedPopup = true
                    )
                }
                return@launch
            }

            if (settings.autoSaveScans) {
                runCatching {
                    saveQrUseCase(detectedQr)
                }.onSuccess {
                    refreshHistoryLimitState()
                    _events.emit(ScannerUiEvent.QrSaved)
                }.onFailure { exception ->
                    _events.emit(
                        ScannerUiEvent.ShowError(
                            message = exception.message ?: "Failed to save QR code"
                        )
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        isSaveBlockedByHistoryLimit = false,
                        showHistoryLimitReachedPopup = false
                    )
                }
            }
        }
    }

    fun onSaveScannedQrClick() {
        val detectedQr = _uiState.value.detectedQr ?: return

        viewModelScope.launch {
            if (!canSaveQrUseCase()) {
                _uiState.update { state ->
                    state.copy(
                        isSaveBlockedByHistoryLimit = true,
                        showHistoryLimitReachedPopup = true
                    )
                }
                return@launch
            }

            runCatching {
                saveQrUseCase(detectedQr)
            }.onSuccess {
                clearScannedQr()
                refreshHistoryLimitState()
                _events.emit(ScannerUiEvent.QrSaved)
            }.onFailure { exception ->
                _events.emit(
                    ScannerUiEvent.ShowError(
                        message = exception.message ?: "Failed to save QR code"
                    )
                )
            }
        }
    }

    private suspend fun refreshHistoryLimitState() {
        val status = getHistoryLimitStatusUseCase()
        _uiState.update { state ->
            state.copy(
                isSaveBlockedByHistoryLimit = status.isLimitReached,
                showHistoryLimitReachedPopup = status.isLimitReached
            )
        }
    }

    fun clearScannedQr() {
        _uiState.update { state ->
            state.copy(mode = ScannerUiState.Mode.Idle, isTorchEnabled = false, detectedQr = null)
        }
    }
}
