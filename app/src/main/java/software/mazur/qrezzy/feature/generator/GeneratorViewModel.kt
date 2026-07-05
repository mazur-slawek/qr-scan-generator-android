package software.mazur.qrezzy.feature.generator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.core.qr.style.QrStyleEditorState
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.usecase.CreateGeneratedQrUseCase
import software.mazur.qrezzy.domain.qr.usecase.SaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.CanSaveQrUseCase
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.mapper.toQrContent
import software.mazur.qrezzy.feature.generator.mapper.toQrType
import software.mazur.qrezzy.feature.generator.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.model.GeneratorUiState
import software.mazur.qrezzy.feature.generator.model.QrFieldError
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs

@HiltViewModel
class GeneratorViewModel
@Inject
constructor(
    private val createGeneratedQrUseCase: CreateGeneratedQrUseCase,
    private val qrBitmapGenerator: QrBitmapGenerator,
    private val saveQrUseCase: SaveQrUseCase,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val canSaveQrUseCase: CanSaveQrUseCase,
    private val getHistoryLimitStatusUseCase: GetHistoryLimitStatusUseCase
) : ViewModel() {
    var uiState = mutableStateOf(createInitialState())
        private set
    private val _events = MutableSharedFlow<GeneratorUiEvent>()
    val events = _events.asSharedFlow()

    init {
        observeHistoryLimit()
    }

    private fun observeHistoryLimit() {
        viewModelScope.launch {
            observeAppSettingsUseCase().collect {
                refreshHistoryLimitState()
            }
        }
    }

    fun onFormEvent(field: QrInputField, value: String) {
        val fieldErrors = updateFieldError(field = field, value = value)

        when (field) {
            QrInputField.Text -> updateSelectedQrInput(
                qrInput = QrInput.Text(text = value),
                fieldErrors = fieldErrors
            )

            QrInputField.Url -> updateSelectedQrInput(
                qrInput = QrInput.Url(url = value),
                fieldErrors = fieldErrors
            )

            QrInputField.Phone -> updateSelectedQrInput(
                qrInput = QrInput.Phone(phoneNumber = value),
                fieldErrors = fieldErrors
            )

            QrInputField.EmailAddress,
            QrInputField.EmailSubject,
            QrInputField.EmailBody -> updateEmailInput(field = field, value = value, fieldErrors = fieldErrors)

            QrInputField.WifiSsid,
            QrInputField.WifiPassword -> updateWifiInput(field = field, value = value, fieldErrors = fieldErrors)

            QrInputField.ContactFirstName,
            QrInputField.ContactLastName,
            QrInputField.ContactPhone,
            QrInputField.ContactEmail,
            QrInputField.ContactCompany -> updateContactInput(field = field, value = value, fieldErrors = fieldErrors)

            QrInputField.SmsPhone,
            QrInputField.SmsMessage -> updateSmsInput(field, value, fieldErrors)

            QrInputField.GeoLatitude,
            QrInputField.GeoLongitude -> updateGeoLocationInput(field, value, fieldErrors)
        }
    }

    fun onQrInputSelected(qrInput: QrInput) {
        val existingInput = uiState.value.qrInputs.firstOrNull { input -> input.isSameTypeAs(qrInput) } ?: qrInput
        updateSelectedQrInput(qrInput = existingInput)
    }

    fun generateQrBitmap(content: String) = qrBitmapGenerator.generate(content = content, style = uiState.value.qrStyle)

    fun generatePreviewQrBitmap(content: String, style: QrStyle) = qrBitmapGenerator.generate(content = content, style = style)

    fun onCustomizeQrClick() {
        updateQrStyleEditor { editor -> editor.open() }
    }

    fun onDismissCustomizeQrDialog() {
        updateQrStyleEditor { editor -> editor.dismiss() }
    }

    fun onQrColorSelected(color: Long) {
        updateQrStyleEditor { editor -> editor.updateQrColor(color) }
    }

    fun onBackgroundColorSelected(color: Long) {
        updateQrStyleEditor { editor -> editor.updateBackgroundColor(color) }
    }

    fun onPatternStyleSelected(patternStyle: QrPatternStyle) {
        updateQrStyleEditor { editor -> editor.updatePatternStyle(patternStyle) }
    }

    fun onErrorCorrectionSelected(errorCorrection: QrErrorCorrection) {
        updateQrStyleEditor { editor -> editor.updateErrorCorrection(errorCorrection) }
    }

    fun onResetQrStyleClick() {
        updateQrStyleEditor { editor -> editor.resetDraft() }
    }

    fun onApplyQrStyleClick() {
        updateQrStyleEditor { editor -> editor.applyDraft() }
    }

    fun saveQrCode() {
        val selectedQrInput = uiState.value.selectedQrInput
        val qrContent = uiState.value.qrContent

        if (qrContent.isBlank()) return

        viewModelScope.launch {
            if (!canSaveQrUseCase()) {
                uiState.value = uiState.value.copy(
                    isSaveBlockedByHistoryLimit = true,
                    showHistoryLimitReachedPopup = true
                )
                return@launch
            }
            val qr = createGeneratedQrUseCase(
                type = selectedQrInput.toQrType(),
                content = qrContent,
                style = uiState.value.qrStyle
            )

            runCatching {
                saveQrUseCase(qr)
            }.onSuccess {
                resetForm()
                _events.emit(GeneratorUiEvent.QrSaved)
            }.onFailure {
                _events.emit(GeneratorUiEvent.QrSaveFailed)
            }
        }
    }

    private fun updateEmailInput(field: QrInputField, value: String, fieldErrors: Map<QrInputField, QrFieldError>) {
        val currentInput = uiState.value.selectedQrInput as? QrInput.Email ?: QrInput.Email()
        val updatedInput = when (field) {
            QrInputField.EmailAddress -> currentInput.copy(email = value)
            QrInputField.EmailSubject -> currentInput.copy(subject = value)
            QrInputField.EmailBody -> currentInput.copy(body = value)
            else -> currentInput
        }
        updateSelectedQrInput(qrInput = updatedInput, fieldErrors = fieldErrors)
    }

    private fun updateWifiInput(field: QrInputField, value: String, fieldErrors: Map<QrInputField, QrFieldError>) {
        val currentInput = uiState.value.selectedQrInput as? QrInput.Wifi ?: QrInput.Wifi()
        val updatedInput = when (field) {
            QrInputField.WifiSsid -> currentInput.copy(ssid = value)
            QrInputField.WifiPassword -> currentInput.copy(password = value)
            else -> currentInput
        }
        updateSelectedQrInput(qrInput = updatedInput, fieldErrors = fieldErrors)
    }

    private fun updateContactInput(field: QrInputField, value: String, fieldErrors: Map<QrInputField, QrFieldError>) {
        val currentInput = uiState.value.selectedQrInput as? QrInput.Contact ?: QrInput.Contact()
        val updatedInput = when (field) {
            QrInputField.ContactFirstName -> currentInput.copy(firstName = value)
            QrInputField.ContactLastName -> currentInput.copy(lastName = value)
            QrInputField.ContactPhone -> currentInput.copy(phone = value)
            QrInputField.ContactEmail -> currentInput.copy(email = value)
            QrInputField.ContactCompany -> currentInput.copy(company = value)
            else -> currentInput
        }
        updateSelectedQrInput(qrInput = updatedInput, fieldErrors = fieldErrors)
    }

    private fun updateSmsInput(field: QrInputField, value: String, fieldErrors: Map<QrInputField, QrFieldError>) {
        val currentInput = uiState.value.selectedQrInput as? QrInput.Sms ?: QrInput.Sms()
        val updatedInput = when (field) {
            QrInputField.SmsPhone -> currentInput.copy(phoneNumber = value)
            QrInputField.SmsMessage -> currentInput.copy(message = value)
            else -> currentInput
        }
        updateSelectedQrInput(qrInput = updatedInput, fieldErrors = fieldErrors)
    }

    private fun updateGeoLocationInput(field: QrInputField, value: String, fieldErrors: Map<QrInputField, QrFieldError>) {
        val currentInput = uiState.value.selectedQrInput as? QrInput.GeoLocation ?: QrInput.GeoLocation()
        val updatedInput = when (field) {
            QrInputField.GeoLatitude -> currentInput.copy(latitude = value)
            QrInputField.GeoLongitude -> currentInput.copy(longitude = value)
            else -> currentInput
        }
        updateSelectedQrInput(qrInput = updatedInput, fieldErrors = fieldErrors)
    }

    private fun updateQrStyleEditor(update: (QrStyleEditorState) -> QrStyleEditorState) {
        uiState.value = uiState.value.copy(qrStyleEditor = update(uiState.value.qrStyleEditor))
    }

    private suspend fun resetForm() {
        val status = getHistoryLimitStatusUseCase()
        uiState.value = createInitialState().copy(
            isSaveBlockedByHistoryLimit = status.isLimitReached,
            showHistoryLimitReachedPopup = status.isLimitReached
        )
    }

    private fun createInitialState(): GeneratorUiState {
        val initialQrInput = QrInput.Text()
        return GeneratorUiState(selectedQrInput = initialQrInput, qrContent = initialQrInput.toQrContent())
    }

    private fun validateField(field: QrInputField, value: String): QrFieldError? = when {
        value.length > field.maxLength -> QrFieldError("Maximum length is ${field.maxLength} characters")
        else -> null
    }

    private fun updateFieldError(field: QrInputField, value: String): Map<QrInputField, QrFieldError> {
        val error = validateField(field = field, value = value)
        return if (error == null) uiState.value.fieldErrors - field else uiState.value.fieldErrors + (field to error)
    }

    private fun updateSelectedQrInput(qrInput: QrInput, fieldErrors: Map<QrInputField, QrFieldError> = uiState.value.fieldErrors) {
        uiState.value = uiState.value.copy(
            selectedQrInput = qrInput,
            qrContent = qrInput.toQrContent(),
            fieldErrors = fieldErrors,
            qrInputs = uiState.value.qrInputs.map { currentInput ->
                if (currentInput.isSameTypeAs(qrInput)) qrInput else currentInput
            }
        )
    }

    fun onScreenOpened() {
        viewModelScope.launch {
            refreshHistoryLimitState()
        }
    }

    private suspend fun refreshHistoryLimitState() {
        val status = getHistoryLimitStatusUseCase()
        uiState.value = uiState.value.copy(
            isSaveBlockedByHistoryLimit = status.isLimitReached,
            showHistoryLimitReachedPopup = status.isLimitReached
        )
    }
}
