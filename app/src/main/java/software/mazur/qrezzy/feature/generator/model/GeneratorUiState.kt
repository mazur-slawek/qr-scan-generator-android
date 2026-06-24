package software.mazur.qrezzy.feature.generator.model

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
    val qrStyle: QrStyle = QrStyle(),
    val draftQrStyle: QrStyle = QrStyle(),
    val isCustomizeQrDialogVisible: Boolean = false,
) {
    val canSave: Boolean
        get() = qrContent.isNotBlank() && fieldErrors.isEmpty()
}
