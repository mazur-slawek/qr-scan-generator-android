package software.mazur.qrezzy.feature.scanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.qr.decoder.QrImageDecoder
import software.mazur.qrezzy.domain.qr.usecase.CreateScannedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState.Mode
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val createScannedQrUseCase: CreateScannedQrUseCase,
    private val saveQrUseCase: SaveQrUseCase,
    private val qrImageDecoder: QrImageDecoder,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<ScannerUiEvent>(
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun onStartScanning() {
        _uiState.update { state -> state.copy(mode = Mode.Scanning, isTorchEnabled = false) }
    }

    fun onStopScanning() {
        _uiState.update { state -> state.copy(mode = Mode.Idle, isTorchEnabled = false, detectedQr = null) }
    }

    fun onPermissionDenied() {
        _uiState.update { state -> state.copy(mode = Mode.PermissionDenied, isTorchEnabled = false) }
    }

    fun onPermissionRestored() {
        _uiState.update { state -> state.copy(mode = Mode.Idle) }
    }

    fun onTorchClick() {
        if (!_uiState.value.isScanning) return
        _uiState.update { state -> state.copy(isTorchEnabled = !state.isTorchEnabled) }
    }

    fun onQrCodeScanned(content: String) {
        val trimmedContent = content.trim()

        if (trimmedContent.isBlank()) return
        if (!_uiState.value.isScanning) return
        if (_uiState.value.detectedQr != null) return
        val detectedQr = createScannedQrUseCase(trimmedContent)
        viewModelScope.launch {
            _uiState.update { state -> state.copy(mode = Mode.Idle, isTorchEnabled = false, detectedQr = detectedQr) }
        }
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
            val detectedQr = createScannedQrUseCase(content)
            _uiState.update { state -> state.copy(mode = Mode.Idle, isTorchEnabled = false, detectedQr = detectedQr) }
        }
    }

    fun onSaveScannedQrClick() {
        val detectedQr = _uiState.value.detectedQr ?: return
        viewModelScope.launch {
            try {
                saveQrUseCase(detectedQr)
                clearScannedQr()
                _events.emit(ScannerUiEvent.QrSaved)
            } catch (exception: Exception) {
                _events.emit(ScannerUiEvent.ShowError(message = exception.message ?: "Failed to save QR code"))
            }
        }
    }

    fun clearScannedQr() {
        _uiState.update { state -> state.copy(mode = Mode.Idle, isTorchEnabled = false, detectedQr = null) }
    }
}