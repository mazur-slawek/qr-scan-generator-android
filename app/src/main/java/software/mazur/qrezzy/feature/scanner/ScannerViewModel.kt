package software.mazur.qrezzy.feature.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.history.usecase.SaveScannedQrUseCase
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState.Mode
import software.mazur.qrezzy.feature.scanner.parser.QrContentParser
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val saveScannedQrUseCase: SaveScannedQrUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<ScannerUiEvent>()
    val events = _events.asSharedFlow()

    fun onStartScanning() {
        _uiState.update { state ->
            state.copy(
                mode = Mode.Scanning,
                isTorchEnabled = false,
                scannedContent = null,
                scannedType = null,
                scannedTitle = null,
            )
        }
    }

    fun onStopScanning() {
        _uiState.update { state ->
            state.copy(
                mode = Mode.Idle,
                isTorchEnabled = false,
            )
        }
    }

    fun onPermissionDenied() {
        _uiState.update { state ->
            state.copy(
                mode = Mode.PermissionDenied,
                isTorchEnabled = false,
            )
        }
    }

    fun onPermissionRestored() {
        _uiState.update { state ->
            state.copy(mode = Mode.Idle)
        }
    }

    fun onTorchClick() {
        if (!_uiState.value.isScanning) return

        _uiState.update { state ->
            state.copy(isTorchEnabled = !state.isTorchEnabled)
        }
    }

    fun onQrCodeScanned(content: String) {
        val trimmedContent = content.trim()

        if (trimmedContent.isBlank()) return
        if (!_uiState.value.isScanning) return
        if (_uiState.value.isDialogVisible) return
        val parsedQr = QrContentParser.parse(trimmedContent)

        _uiState.update { state ->
            state.copy(
                mode = Mode.Idle,
                isTorchEnabled = false,
                scannedContent = trimmedContent,
                scannedType = parsedQr.type,
                scannedTitle = parsedQr.title,
            )
        }
    }

    fun onSaveScannedQrClick() {
        val state = _uiState.value
        val content = state.scannedContent ?: return
        val type = state.scannedType ?: return
        val title = state.scannedTitle ?: return

        viewModelScope.launch {
            try {
                saveScannedQrUseCase(type = type, title = title, content = content, payloadJson = null)
                clearScannedQr()
                _events.emit(ScannerUiEvent.QrSaved)
            } catch (exception: Exception) {
                _events.emit(
                    ScannerUiEvent.ShowError(message = exception.message ?: "Failed to save QR code")
                )
            }
        }
    }

    fun onCancelScannedQrClick() {
        clearScannedQr()
    }

    private fun clearScannedQr() {
        _uiState.update { state ->
            state.copy(
                mode = Mode.Idle,
                isTorchEnabled = false,
                scannedContent = null,
                scannedType = null,
                scannedTitle = null,
            )
        }
    }
}