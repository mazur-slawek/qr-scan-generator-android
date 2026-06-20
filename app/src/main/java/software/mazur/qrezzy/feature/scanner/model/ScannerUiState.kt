package software.mazur.qrezzy.feature.scanner.model

import software.mazur.qrezzy.domain.qr.model.Qr

data class ScannerUiState(
    val mode: Mode = Mode.Idle,
    val isTorchEnabled: Boolean = false,
    val detectedQr: Qr? = null,
) {
    enum class Mode { Idle, Scanning, PermissionDenied }

    val isScanning: Boolean get() = mode == Mode.Scanning
}
