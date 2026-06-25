package software.mazur.qrezzy.feature.generator.model

import software.mazur.qrezzy.core.qr.style.QrStyleEditorState
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

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
        ),
    val fieldErrors: Map<QrInputField, QrFieldError> = emptyMap(),
    val qrStyleEditor: QrStyleEditorState = QrStyleEditorState()
) {
    val canSave: Boolean
        get() = qrContent.isNotBlank() && fieldErrors.isEmpty()
    val qrStyle: QrStyle
        get() = qrStyleEditor.appliedStyle
    val draftQrStyle: QrStyle
        get() = qrStyleEditor.draftStyle
    val isCustomizeQrDialogVisible: Boolean
        get() = qrStyleEditor.isDialogVisible
}
