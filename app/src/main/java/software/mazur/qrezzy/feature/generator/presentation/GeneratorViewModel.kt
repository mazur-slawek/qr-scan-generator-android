package software.mazur.qrezzy.feature.generator.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.history.usecase.SaveGeneratedQrUseCase
import software.mazur.qrezzy.feature.generator.domain.GenerateQrBitmapUseCase
import software.mazur.qrezzy.feature.generator.domain.GenerateQrContentUseCase
import software.mazur.qrezzy.feature.generator.mapper.toHistoryTitle
import software.mazur.qrezzy.feature.generator.mapper.toHistoryType
import software.mazur.qrezzy.feature.generator.mapper.toPayloadJson
import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import software.mazur.qrezzy.feature.generator.presentation.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.presentation.model.GeneratorUiState
import software.mazur.qrezzy.feature.generator.presentation.model.QrFieldError
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactCompany
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactEmail
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactFirstName
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactLastName
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactPhone
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailAddress
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailBody
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailSubject
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Phone
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Text
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Url
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.WifiPassword
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.WifiSsid
import software.mazur.qrezzy.feature.generator.presentation.model.maxLength
import javax.inject.Inject

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val generateQrContentUseCase: GenerateQrContentUseCase,
    private val generateQrBitmapUseCase: GenerateQrBitmapUseCase,
    private val saveGeneratedQrUseCase: SaveGeneratedQrUseCase,
) : ViewModel() {
    var uiState = mutableStateOf(createInitialState())
        private set
    private val _events = MutableSharedFlow<GeneratorUiEvent>()
    val events = _events.asSharedFlow()

    fun onFormEvent(field: QrTypeField, value: String) {
        val fieldErrors = updateFieldError(field, value)

        when (field) {
            Text                                                                          -> {
                updateSelectedType(QrType.Text(text = value), fieldErrors)
            }

            Url                                                                           -> {
                updateSelectedType(QrType.Url(url = value), fieldErrors)
            }

            Phone                                                                         -> {
                updateSelectedType(QrType.Phone(phoneNumber = value), fieldErrors)
            }

            EmailAddress, EmailSubject, EmailBody                                         -> {
                val current = uiState.value.selectedType as? QrType.Email ?: QrType.Email()

                when (field) {
                    EmailAddress -> updateSelectedType(current.copy(email = value), fieldErrors)
                    EmailSubject -> updateSelectedType(current.copy(subject = value), fieldErrors)
                    EmailBody    -> updateSelectedType(current.copy(body = value), fieldErrors)
                    else         -> Unit
                }
            }

            WifiSsid, WifiPassword                                                        -> {
                val current = uiState.value.selectedType as? QrType.Wifi ?: QrType.Wifi()

                when (field) {
                    WifiSsid     -> updateSelectedType(current.copy(ssid = value), fieldErrors)
                    WifiPassword -> updateSelectedType(current.copy(password = value), fieldErrors)
                    else         -> Unit
                }
            }

            ContactFirstName, ContactLastName, ContactPhone, ContactEmail, ContactCompany -> {
                val current = uiState.value.selectedType as? QrType.Contact ?: QrType.Contact()

                when (field) {
                    ContactFirstName -> updateSelectedType(current.copy(firstName = value), fieldErrors)
                    ContactLastName  -> updateSelectedType(current.copy(lastName = value), fieldErrors)
                    ContactPhone     -> updateSelectedType(current.copy(phone = value), fieldErrors)
                    ContactEmail     -> updateSelectedType(current.copy(email = value), fieldErrors)
                    ContactCompany   -> updateSelectedType(current.copy(company = value), fieldErrors)
                    else             -> Unit
                }
            }
        }
    }

    fun onTypeSelected(type: QrType) {
        val existingType = uiState.value.qrTypes.firstOrNull { it.isSameTypeAs(type) } ?: type
        updateSelectedType(existingType)
    }

    fun generateQrBitmap(content: String) = generateQrBitmapUseCase(content)

    fun saveQrCode() {
        val selectedType = uiState.value.selectedType
        val qrContent = uiState.value.qrContent

        if (qrContent.isBlank()) return

        viewModelScope.launch {
            saveGeneratedQrUseCase(
                type = selectedType.toHistoryType(),
                title = selectedType.toHistoryTitle(),
                content = qrContent,
                payloadJson = selectedType.toPayloadJson(),
            )

            resetForm()
            _events.emit(GeneratorUiEvent.QrSaved)
        }
    }

    private fun resetForm() {
        uiState.value = createInitialState()
    }

    private fun createInitialState(): GeneratorUiState {
        val initialType = QrType.Text()

        return GeneratorUiState(
            selectedType = initialType,
            qrContent = generateQrContentUseCase(initialType),
        )
    }

    private fun validateField(field: QrTypeField, value: String): QrFieldError? {
        return when {
            value.length > field.maxLength -> {
                QrFieldError("Maximum length is ${field.maxLength} characters")
            }

            else                           -> null
        }
    }

    private fun updateFieldError(field: QrTypeField, value: String): Map<QrTypeField, QrFieldError> {
        val error = validateField(field, value)
        return if (error == null) {
            uiState.value.fieldErrors - field
        } else {
            uiState.value.fieldErrors + (field to error)
        }
    }

    private fun updateSelectedType(
        type: QrType,
        fieldErrors: Map<QrTypeField, QrFieldError> = uiState.value.fieldErrors
    ) {
        uiState.value = uiState.value.copy(
            selectedType = type,
            qrContent = generateQrContentUseCase(type),
            fieldErrors = fieldErrors,
            qrTypes = uiState.value.qrTypes.map { currentType ->
                if (currentType.isSameTypeAs(type)) type else currentType
            },
        )
    }
}