package software.mazur.qrezzy.feature.generator.model

import software.mazur.qrezzy.core.qr.style.QrStyleEditorState
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.feature.generator.mapper.fields

data class GeneratorUiState(
    val selectedQrInput: QrInput = QrInput.Text(),
    val qrContent: String = "",
    val qrInputs: List<QrInput> =
        listOf(
            QrInput.Text(),
            QrInput.Url(),
            QrInput.Wifi(),
            QrInput.Contact(),
            QrInput.Email(),
            QrInput.Phone(),
            QrInput.Sms(),
            QrInput.GeoLocation()
        ),
    val fieldErrors: Map<QrInputField, QrFieldError> = emptyMap(),
    val qrStyleEditor: QrStyleEditorState = QrStyleEditorState(),
    val isSaveBlockedByHistoryLimit: Boolean = false,
    val showHistoryLimitReachedPopup: Boolean = false
) {
    val canSave: Boolean
        get() = qrContent.isNotBlank() && currentFieldErrors.isEmpty() && !isSaveBlockedByHistoryLimit
    val currentFieldErrors: Map<QrInputField, QrFieldError>
        get() = fieldErrors.filterKeys { field -> field in selectedQrInput.fields }
    val qrStyle: QrStyle
        get() = qrStyleEditor.appliedStyle
}
