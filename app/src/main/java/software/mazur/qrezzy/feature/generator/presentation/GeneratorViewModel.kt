package software.mazur.qrezzy.feature.generator.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.qr.usecase.SaveGeneratedQrItemUseCase
import software.mazur.qrezzy.feature.generator.domain.GenerateQrBitmapUseCase
import software.mazur.qrezzy.feature.generator.domain.GenerateQrContentUseCase
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.mapper.toQrPayloadJson
import software.mazur.qrezzy.feature.generator.mapper.toQrTitle
import software.mazur.qrezzy.feature.generator.mapper.toQrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import software.mazur.qrezzy.feature.generator.presentation.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.presentation.model.GeneratorUiState
import software.mazur.qrezzy.feature.generator.presentation.model.QrFieldError
import javax.inject.Inject

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val generateQrContentUseCase: GenerateQrContentUseCase,
    private val generateQrBitmapUseCase: GenerateQrBitmapUseCase,
    private val saveGeneratedQrItemUseCase: SaveGeneratedQrItemUseCase,
) : ViewModel() {
    var uiState = mutableStateOf(createInitialState())
        private set
    private val _events = MutableSharedFlow<GeneratorUiEvent>()
    val events = _events.asSharedFlow()

    fun onFormEvent(field: QrInputField, value: String) {
        val fieldErrors = updateFieldError(field, value)

        when (field) {
            QrInputField.Text                                                                                                                              -> {
                updateSelectedQrInput(QrInput.Text(text = value), fieldErrors)
            }

            QrInputField.Url                                                                                                                               -> {
                updateSelectedQrInput(QrInput.Url(url = value), fieldErrors)
            }

            QrInputField.Phone                                                                                                                             -> {
                updateSelectedQrInput(QrInput.Phone(phoneNumber = value), fieldErrors)
            }

            QrInputField.EmailAddress, QrInputField.EmailSubject, QrInputField.EmailBody                                                                   -> {
                val current = uiState.value.selectedQrInput as? QrInput.Email ?: QrInput.Email()

                when (field) {
                    QrInputField.EmailAddress -> updateSelectedQrInput(current.copy(email = value), fieldErrors)
                    QrInputField.EmailSubject -> updateSelectedQrInput(current.copy(subject = value), fieldErrors)
                    QrInputField.EmailBody    -> updateSelectedQrInput(current.copy(body = value), fieldErrors)
                    else                      -> Unit
                }
            }

            QrInputField.WifiSsid, QrInputField.WifiPassword                                                                                               -> {
                val current = uiState.value.selectedQrInput as? QrInput.Wifi ?: QrInput.Wifi()

                when (field) {
                    QrInputField.WifiSsid     -> updateSelectedQrInput(current.copy(ssid = value), fieldErrors)
                    QrInputField.WifiPassword -> updateSelectedQrInput(current.copy(password = value), fieldErrors)
                    else                      -> Unit
                }
            }

            QrInputField.ContactFirstName, QrInputField.ContactLastName, QrInputField.ContactPhone, QrInputField.ContactEmail, QrInputField.ContactCompany -> {
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

    fun generateQrBitmap(content: String) = generateQrBitmapUseCase(content)

    fun saveQrCode() {
        val selectedQrInput = uiState.value.selectedQrInput
        val qrContent = uiState.value.qrContent

        if (qrContent.isBlank()) return

        viewModelScope.launch {
            saveGeneratedQrItemUseCase(
                type = selectedQrInput.toQrType(),
                title = selectedQrInput.toQrTitle(),
                content = qrContent,
                payloadJson = selectedQrInput.toQrPayloadJson(),
            )

            resetForm()
            _events.emit(GeneratorUiEvent.QrSaved)
        }
    }

    private fun resetForm() {
        uiState.value = createInitialState()
    }

    private fun createInitialState(): GeneratorUiState {
        val initialQrInput = QrInput.Text()

        return GeneratorUiState(
            selectedQrInput = initialQrInput,
            qrContent = generateQrContentUseCase(initialQrInput),
        )
    }

    private fun validateField(field: QrInputField, value: String): QrFieldError? {
        return when {
            value.length > field.maxLength -> {
                QrFieldError("Maximum length is ${field.maxLength} characters")
            }

            else                           -> null
        }
    }

    private fun updateFieldError(field: QrInputField, value: String): Map<QrInputField, QrFieldError> {
        val error = validateField(field, value)
        return if (error == null) {
            uiState.value.fieldErrors - field
        } else {
            uiState.value.fieldErrors + (field to error)
        }
    }

    private fun updateSelectedQrInput(
        qrInput: QrInput,
        fieldErrors: Map<QrInputField, QrFieldError> = uiState.value.fieldErrors
    ) {
        uiState.value = uiState.value.copy(
            selectedQrInput = qrInput,
            qrContent = generateQrContentUseCase(qrInput),
            fieldErrors = fieldErrors,
            qrInputs = uiState.value.qrInputs.map { currentType ->
                if (currentType.isSameTypeAs(qrInput)) qrInput else currentType
            },
        )
    }
}