package software.mazur.qrezzy.feature.generator.model

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
) {
    val canSave: Boolean
        get() = qrContent.isNotBlank() && fieldErrors.isEmpty()
}
