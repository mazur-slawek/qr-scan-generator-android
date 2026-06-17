package software.mazur.qrezzy.feature.scanner.model

sealed interface ScannerUiEvent {
    data object QrSaved : ScannerUiEvent
    data class ShowError(val message: String) : ScannerUiEvent
}