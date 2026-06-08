package software.mazur.qrezzy.feature.generator.presentation.model

import software.mazur.qrezzy.feature.generator.model.QrType

data class GeneratorUiState(
    val selectedType: QrType = QrType.Text(),
    val qrContent: String = "",
    val qrTypes: List<QrType> = listOf(
        QrType.Text(),
        QrType.Url(),
        QrType.Wifi(),
        QrType.Contact(),
        QrType.Email(),
        QrType.Phone(),
    ),
    val fieldErrors: Map<QrTypeField, QrFieldError> = emptyMap(),
) {
    val canSave: Boolean
        get() = qrContent.isNotBlank() && fieldErrors.isEmpty()
}