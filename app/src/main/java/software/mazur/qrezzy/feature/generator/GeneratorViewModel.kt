package software.mazur.qrezzy.feature.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.common.crash.CrashReporter
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.core.qr.renderer.QrGenerationResult
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
import software.mazur.qrezzy.feature.generator.model.QrGenerationError
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.model.QrPreviewState
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import software.mazur.qrezzy.feature.generator.model.toPendingState

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val crashReporter: CrashReporter,
    private val createGeneratedQrUseCase: CreateGeneratedQrUseCase,
    private val qrBitmapGenerator: QrBitmapGenerator,
    private val saveQrUseCase: SaveQrUseCase,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val canSaveQrUseCase: CanSaveQrUseCase,
    private val getHistoryLimitStatusUseCase: GetHistoryLimitStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(createInitialState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<GeneratorUiEvent>()
    val events = _events.asSharedFlow()

    init {
        observeHistoryLimit()
        observeQrPreview()
        observeEditPreview()
    }

    private fun observeHistoryLimit() {
        viewModelScope.launch {
            observeAppSettingsUseCase().collect {
                refreshHistoryLimitState()
            }
        }
    }

    /**
     * Zmiana treści jest debounce'owana (pisanie w formularzu), zmiana zastosowanego stylu (Apply)
     * reaguje natychmiast — mapLatest i tak anuluje nieaktualną generację niezależnie od źródła.
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeQrPreview() {
        val debouncedContent = uiState
            .map { it.qrContent }
            .distinctUntilChanged()
            .debounce(QR_CONTENT_DEBOUNCE_MS)
        val style = uiState.map { it.qrStyle }.distinctUntilChanged()

        combine(debouncedContent, style) { content, qrStyle -> content to qrStyle }
            .distinctUntilChanged()
            .mapLatest { (content, qrStyle) -> renderPreview(content, qrStyle) }
            .onEach { preview -> updateUiState { it.copy(qrPreview = preview) } }
            .launchIn(viewModelScope)
    }

    /** Zmiany w edytorze stylu (kolor/wzór/korekcja błędów) to dyskretne kliknięcia — bez debounce. */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeEditPreview() {
        uiState
            .filter { it.qrStyleEditor.isDialogVisible }
            .map { it.qrContent to it.qrStyleEditor.draftStyle }
            .distinctUntilChanged()
            .mapLatest { (content, style) -> renderPreview(content, style) }
            .onEach { preview -> updateUiState { it.copy(editPreview = preview) } }
            .launchIn(viewModelScope)
    }

    private suspend fun renderPreview(content: String, style: QrStyle): QrPreviewState {
        if (content.isBlank()) return QrPreviewState.Idle

        return when (val result = withContext(Dispatchers.Default) { qrBitmapGenerator.generate(content = content, style = style) }) {
            null -> QrPreviewState.Idle
            is QrGenerationResult.Success -> QrPreviewState.Ready(result.bitmap)
            QrGenerationResult.CannotEncode -> QrPreviewState.Error(QrGenerationError.CannotEncode)
            is QrGenerationResult.Failed -> {
                crashReporter.recordException(result.throwable)
                QrPreviewState.Error(QrGenerationError.Unknown)
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

        crashReporter.log("Generator: save QR requested")

        viewModelScope.launch {
            if (!canSaveQrUseCase()) {
                crashReporter.log("Generator: save blocked by history limit")

                updateUiState { it.copy(isSaveBlockedByHistoryLimit = true, showHistoryLimitReachedPopup = true) }
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
                crashReporter.log("Generator: QR saved successfully")
                resetForm()
                _events.emit(GeneratorUiEvent.QrSaved)
            }.onFailure {
                crashReporter.log("Generator: QR save failed")
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
        updateUiState { state -> state.copy(qrStyleEditor = update(state.qrStyleEditor)) }
    }

    private suspend fun resetForm() {
        val status = getHistoryLimitStatusUseCase()
        updateUiState {
            createInitialState().copy(
                isSaveBlockedByHistoryLimit = status.isLimitReached,
                showHistoryLimitReachedPopup = status.isLimitReached
            )
        }
    }

    private fun createInitialState(): GeneratorUiState {
        val initialQrInput = QrInput.Text()
        return GeneratorUiState(selectedQrInput = initialQrInput, qrContent = initialQrInput.toQrContent())
    }

    private fun validateField(field: QrInputField, value: String): QrFieldError? = when {
        value.length > field.maxLength -> QrFieldError.MaxLength(field.maxLength)
        else -> null
    }

    private fun updateFieldError(field: QrInputField, value: String): Map<QrInputField, QrFieldError> {
        val error = validateField(field = field, value = value)
        return if (error == null) uiState.value.fieldErrors - field else uiState.value.fieldErrors + (field to error)
    }

    private fun updateSelectedQrInput(qrInput: QrInput, fieldErrors: Map<QrInputField, QrFieldError> = uiState.value.fieldErrors) {
        updateUiState { state ->
            state.copy(
                selectedQrInput = qrInput,
                qrContent = qrInput.toQrContent(),
                fieldErrors = fieldErrors,
                qrInputs = state.qrInputs.map { currentInput ->
                    if (currentInput.isSameTypeAs(qrInput)) qrInput else currentInput
                }
            )
        }
    }

    fun onScreenOpened() {
        viewModelScope.launch {
            refreshHistoryLimitState()
        }
    }

    private suspend fun refreshHistoryLimitState() {
        val status = getHistoryLimitStatusUseCase()
        updateUiState {
            it.copy(
                isSaveBlockedByHistoryLimit = status.isLimitReached,
                showHistoryLimitReachedPopup = status.isLimitReached
            )
        }
    }

    /**
     * Jedyny punkt mutacji stanu. Gdy zmienia się treść/styl (zastosowany lub roboczy), odpowiedni
     * podgląd jest natychmiast oznaczany jako nieaktualny (Generating) w tej samej aktualizacji —
     * canSave nigdy nie odnosi się do przeterminowanej treści/stylu.
     */
    private fun updateUiState(transform: (GeneratorUiState) -> GeneratorUiState) {
        _uiState.update { state ->
            val next = transform(state)
            val qrPreview = if (next.qrContent != state.qrContent || next.qrStyle != state.qrStyle) {
                next.qrPreview.toPendingState(next.qrContent)
            } else {
                next.qrPreview
            }

            val dialogJustOpened = next.qrStyleEditor.isDialogVisible && !state.qrStyleEditor.isDialogVisible
            val draftChanged = next.qrContent != state.qrContent || next.qrStyleEditor.draftStyle != state.qrStyleEditor.draftStyle
            val editPreview = if (next.qrStyleEditor.isDialogVisible && (dialogJustOpened || draftChanged)) {
                next.editPreview.toPendingState(next.qrContent)
            } else {
                next.editPreview
            }
            next.copy(qrPreview = qrPreview, editPreview = editPreview)
        }
    }

    private companion object {
        const val QR_CONTENT_DEBOUNCE_MS = 300L
    }
}
