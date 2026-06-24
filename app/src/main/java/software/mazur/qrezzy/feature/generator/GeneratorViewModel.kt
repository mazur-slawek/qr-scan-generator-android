package software.mazur.qrezzy.feature.generator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.usecase.CreateGeneratedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.mapper.toQrContent
import software.mazur.qrezzy.feature.generator.mapper.toQrType
import software.mazur.qrezzy.feature.generator.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.model.GeneratorUiState
import software.mazur.qrezzy.feature.generator.model.QrFieldError
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import javax.inject.Inject

@HiltViewModel
class GeneratorViewModel
@Inject
constructor(
    private val createGeneratedQrUseCase: CreateGeneratedQrUseCase,
    private val qrBitmapGenerator: QrBitmapGenerator,
    private val saveQrUseCase: SaveQrUseCase,
) : ViewModel() {
    var uiState = mutableStateOf(createInitialState())
        private set
    private val _events = MutableSharedFlow<GeneratorUiEvent>()
    val events = _events.asSharedFlow()

    fun onFormEvent(field: QrInputField, value: String) {
        val fieldErrors = updateFieldError(field, value)

        when (field) {
            QrInputField.Text  -> {
                updateSelectedQrInput(QrInput.Text(text = value), fieldErrors)
            }

            QrInputField.Url   -> {
                updateSelectedQrInput(QrInput.Url(url = value), fieldErrors)
            }

            QrInputField.Phone -> {
                updateSelectedQrInput(QrInput.Phone(phoneNumber = value), fieldErrors)
            }

            QrInputField.EmailAddress,
            QrInputField.EmailSubject,
            QrInputField.EmailBody,
                               -> {
                val current = uiState.value.selectedQrInput as? QrInput.Email ?: QrInput.Email()

                when (field) {
                    QrInputField.EmailAddress -> updateSelectedQrInput(current.copy(email = value), fieldErrors)
                    QrInputField.EmailSubject -> updateSelectedQrInput(current.copy(subject = value), fieldErrors)
                    QrInputField.EmailBody    -> updateSelectedQrInput(current.copy(body = value), fieldErrors)
                    else                      -> Unit
                }
            }

            QrInputField.WifiSsid,
            QrInputField.WifiPassword,
                               -> {
                val current = uiState.value.selectedQrInput as? QrInput.Wifi ?: QrInput.Wifi()

                when (field) {
                    QrInputField.WifiSsid     -> updateSelectedQrInput(current.copy(ssid = value), fieldErrors)
                    QrInputField.WifiPassword -> updateSelectedQrInput(current.copy(password = value), fieldErrors)
                    else                      -> Unit
                }
            }

            QrInputField.ContactFirstName,
            QrInputField.ContactLastName,
            QrInputField.ContactPhone,
            QrInputField.ContactEmail,
            QrInputField.ContactCompany,
                               -> {
                val current = uiState.value.selectedQrInput as? QrInput.Contact ?: QrInput.Contact()

                when (field) {
                    QrInputField.ContactFirstName -> updateSelectedQrInput(current.copy(firstName = value), fieldErrors)
                    QrInputField.ContactLastName  -> updateSelectedQrInput(current.copy(lastName = value), fieldErrors)
                    QrInputField.ContactPhone     -> updateSelectedQrInput(current.copy(phone = value), fieldErrors)
                    QrInputField.ContactEmail     -> updateSelectedQrInput(current.copy(email = value), fieldErrors)
                    QrInputField.ContactCompany   -> updateSelectedQrInput(current.copy(company = value), fieldErrors)
                    else                          -> Unit
                }
            }
        }
    }

    fun onQrInputSelected(qrInput: QrInput) {
        val existingInput = uiState.value.qrInputs.firstOrNull { it.isSameTypeAs(qrInput) } ?: qrInput
        updateSelectedQrInput(existingInput)
    }

    fun generateQrBitmap(content: String) = qrBitmapGenerator.generate(content = content, style = uiState.value.qrStyle)

    fun generatePreviewQrBitmap(content: String, style: QrStyle) =
        qrBitmapGenerator.generate(content = content, style = style)

    fun onCustomizeQrClick() {
        uiState.value = uiState.value.copy(draftQrStyle = uiState.value.qrStyle, isCustomizeQrDialogVisible = true)
    }

    fun onDismissCustomizeQrDialog() {
        uiState.value = uiState.value.copy(isCustomizeQrDialogVisible = false)
    }

    fun onQrColorSelected(color: Long) {
        uiState.value = uiState.value.copy(draftQrStyle = uiState.value.draftQrStyle.copy(qrColor = color))
    }

    fun onBackgroundColorSelected(color: Long) {
        uiState.value = uiState.value.copy(draftQrStyle = uiState.value.draftQrStyle.copy(backgroundColor = color))
    }

    fun onPatternStyleSelected(patternStyle: QrPatternStyle) {
        uiState.value = uiState.value.copy(draftQrStyle = uiState.value.draftQrStyle.copy(patternStyle = patternStyle))
    }

    fun onErrorCorrectionSelected(errorCorrection: QrErrorCorrection) {
        uiState.value =
            uiState.value.copy(draftQrStyle = uiState.value.draftQrStyle.copy(errorCorrection = errorCorrection))
    }

    fun onResetQrStyleClick() {
        uiState.value = uiState.value.copy(draftQrStyle = QrStyle())
    }

    fun onApplyQrStyleClick() {
        uiState.value = uiState.value.copy(qrStyle = uiState.value.draftQrStyle, isCustomizeQrDialogVisible = false)
    }

    fun saveQrCode() {
        val selectedQrInput = uiState.value.selectedQrInput
        val qrContent = uiState.value.qrContent

        if (qrContent.isBlank()) return
        val qrType = selectedQrInput.toQrType()
        val qr = createGeneratedQrUseCase(type = qrType, content = qrContent)

        viewModelScope.launch {
            saveQrUseCase(qr)
            resetForm()
            _events.emit(GeneratorUiEvent.QrSaved)
        }
    }

    private fun resetForm() {
        uiState.value = createInitialState()
    }

    private fun createInitialState(): GeneratorUiState {
        val initialQrInput = QrInput.Text()
        return GeneratorUiState(selectedQrInput = initialQrInput, qrContent = initialQrInput.toQrContent())
    }

    private fun validateField(field: QrInputField, value: String): QrFieldError? = when {
        value.length > field.maxLength -> QrFieldError("Maximum length is ${field.maxLength} characters")
        else                           -> null
    }

    private fun updateFieldError(field: QrInputField, value: String): Map<QrInputField, QrFieldError> {
        val error = validateField(field, value)
        return if (error == null) uiState.value.fieldErrors - field else uiState.value.fieldErrors + (field to error)
    }

    private fun updateSelectedQrInput(
        qrInput: QrInput,
        fieldErrors: Map<QrInputField, QrFieldError> = uiState.value.fieldErrors,
    ) {
        uiState.value = uiState.value.copy(
            selectedQrInput = qrInput,
            qrContent = qrInput.toQrContent(),
            fieldErrors = fieldErrors,
            qrInputs = uiState.value.qrInputs.map { currentType ->
                if (currentType.isSameTypeAs(qrInput)) qrInput else currentType
            },
        )
    }
}
