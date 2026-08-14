package software.mazur.qrezzy.feature.scanner.model

sealed interface ScannerUiEvent {
    data object QrSaved : ScannerUiEvent
    data object QrSaveFailed : ScannerUiEvent
    data object NoQrFoundInImage : ScannerUiEvent
}
