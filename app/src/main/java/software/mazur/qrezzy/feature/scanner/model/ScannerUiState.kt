package software.mazur.qrezzy.feature.scanner.model

import software.mazur.qrezzy.domain.history.model.QrHistoryType

data class ScannerUiState(
    val mode: Mode = Mode.Idle,
    val isTorchEnabled: Boolean = false,
    val scannedContent: String? = null,
    val scannedType: QrHistoryType? = null,
    val scannedTitle: String? = null,
) {
    enum class Mode {
        Idle,
        Scanning,
        PermissionDenied,
    }

    val isScanning: Boolean
        get() = mode == Mode.Scanning
    val isDialogVisible: Boolean
        get() = scannedContent != null
}