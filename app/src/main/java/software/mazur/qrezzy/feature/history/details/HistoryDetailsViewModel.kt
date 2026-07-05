package software.mazur.qrezzy.feature.history.details

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.core.qr.style.QrStyleEditorState
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.usecase.DeleteQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.GetQrByIdUseCase
import software.mazur.qrezzy.domain.qr.usecase.ToggleQrFavoriteUseCase
import software.mazur.qrezzy.domain.qr.usecase.UpdateQrStyleUseCase
import software.mazur.qrezzy.feature.history.HistoryRoute
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiState

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQrByIdUseCase: GetQrByIdUseCase,
    private val qrBitmapGenerator: QrBitmapGenerator,
    private val deleteQrItemsUseCase: DeleteQrItemsUseCase,
    private val toggleQrFavoriteUseCase: ToggleQrFavoriteUseCase,
    private val updateQrStyleUseCase: UpdateQrStyleUseCase
) : ViewModel() {
    private val historyId: Long = checkNotNull(savedStateHandle[HistoryRoute.Details.HISTORY_ID_ARG])
    private val _uiState = MutableStateFlow(HistoryDetailsUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<HistoryDetailsUiEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            loadQr()
        }
    }

    fun onShareQrCodeClick() {
        val shareData = getShareData() ?: return

        viewModelScope.launch {
            _events.emit(
                HistoryDetailsUiEvent.ShareQrCode(title = shareData.fileName, bitmap = shareData.bitmap)
            )
        }
    }

    fun onDownloadQrCodeClick() {
        val shareData = getShareData() ?: return

        viewModelScope.launch {
            _events.emit(
                HistoryDetailsUiEvent.DownloadQrCode(fileName = shareData.fileName, bitmap = shareData.bitmap)
            )
        }
    }

    fun onDeleteQrCodeClick() {
        _uiState.update { it.copy(isDeleteConfirmationVisible = true) }
    }

    fun onDeleteConfirmationDialogDismiss() {
        _uiState.update { it.copy(isDeleteConfirmationVisible = false) }
    }

    fun onDeleteConfirmationDialogConfirm() {
        viewModelScope.launch {
            deleteQrItemsUseCase(ids = listOf(historyId))
            _uiState.update { it.copy(isDeleteConfirmationVisible = false) }
            _events.emit(HistoryDetailsUiEvent.OnBack)
        }
    }

    fun onFavoriteClick() {
        val currentQr = _uiState.value.qr ?: return
        val updatedQr = currentQr.copy(isFavorite = !currentQr.isFavorite)

        _uiState.update { state -> state.copy(qr = updatedQr) }

        viewModelScope.launch {
            runCatching {
                toggleQrFavoriteUseCase(id = updatedQr.id, isFavorite = updatedQr.isFavorite)
            }.onFailure {
                _uiState.update { state -> state.copy(qr = currentQr) }
            }
        }
    }

    private suspend fun loadQr() {
        val qr = getQrByIdUseCase(historyId)

        if (qr == null) {
            _uiState.update { it.copy(isLoading = false, isMissing = true) }
            return
        }

        _uiState.update {
            it.copy(
                qr = qr,
                isLoading = true,
                isMissing = false,
                qrStyleEditor = QrStyleEditorState(appliedStyle = qr.style, draftStyle = qr.style)
            )
        }
        val qrBitmap = withContext(Dispatchers.Default) {
            qrBitmapGenerator.generate(content = qr.content, style = qr.style)
        }

        _uiState.update { it.copy(qrBitmap = qrBitmap, isLoading = false) }
    }

    fun onCustomizeQrClick() {
        val qr = _uiState.value.qr ?: return
        _uiState.update { state ->
            state.copy(qrStyleEditor = state.qrStyleEditor.open(), editPreviewBitmap = state.qrBitmap)
        }
        generateEditPreview(content = qr.content)
    }

    fun onDismissCustomizeQrDialog() {
        _uiState.update { state ->
            state.copy(qrStyleEditor = state.qrStyleEditor.dismiss(), editPreviewBitmap = null)
        }
    }

    fun onQrColorSelected(color: Long) {
        updateQrStyleEditor { it.updateQrColor(color) }
    }

    fun onBackgroundColorSelected(color: Long) {
        updateQrStyleEditor { it.updateBackgroundColor(color) }
    }

    fun onPatternStyleSelected(patternStyle: QrPatternStyle) {
        updateQrStyleEditor { it.updatePatternStyle(patternStyle) }
    }

    fun onErrorCorrectionSelected(errorCorrection: QrErrorCorrection) {
        updateQrStyleEditor { it.updateErrorCorrection(errorCorrection) }
    }

    fun onResetQrStyleClick() {
        updateQrStyleEditor { it.resetDraft() }
    }

    fun onSaveQrStyleClick() {
        val currentState = _uiState.value
        val currentQr = currentState.qr ?: return
        val previousBitmap = currentState.qrBitmap
        val previousEditor = currentState.qrStyleEditor
        val newStyle = currentState.qrStyleEditor.draftStyle
        val updatedQr = currentQr.copy(style = newStyle)
        val previewBitmap = currentState.editPreviewBitmap ?: currentState.qrBitmap
        _uiState.update {
            it.copy(
                qr = updatedQr,
                qrBitmap = previewBitmap,
                editPreviewBitmap = null,
                qrStyleEditor = it.qrStyleEditor.applyDraft()
            )
        }
        viewModelScope.launch {
            runCatching {
                updateQrStyleUseCase(id = currentQr.id, style = newStyle)
            }.onSuccess {
                _events.emit(HistoryDetailsUiEvent.QrStyleSaved)
            }.onFailure {
                _uiState.update {
                    it.copy(
                        qr = currentQr,
                        qrBitmap = previousBitmap,
                        editPreviewBitmap = null,
                        qrStyleEditor = previousEditor.dismiss()
                    )
                }
                _events.emit(HistoryDetailsUiEvent.QrStyleSaveFailed)
            }
        }
    }

    private fun updateQrStyleEditor(update: (QrStyleEditorState) -> QrStyleEditorState) {
        val qr = _uiState.value.qr ?: return
        _uiState.update { state -> state.copy(qrStyleEditor = update(state.qrStyleEditor)) }
        generateEditPreview(content = qr.content)
    }

    private fun generateEditPreview(content: String) {
        val draftStyle = _uiState.value.qrStyleEditor.draftStyle

        viewModelScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                qrBitmapGenerator.generate(content = content, style = draftStyle)
            }
            _uiState.update { state -> state.copy(editPreviewBitmap = bitmap) }
        }
    }

    private fun getShareData(): QrShareData? {
        val state = _uiState.value
        val qr = state.qr ?: return null
        val bitmap = state.qrBitmap ?: return null
        return QrShareData(fileName = qr.content.toQrFileName(), bitmap = bitmap)
    }

    private fun String.toQrFileName(): String = trim()
        .take(QR_SHARE_TITLE_MAX_LENGTH)
        .replace(Regex("\\s+"), "_")
        .ifBlank { DEFAULT_FILE_NAME }

    private data class QrShareData(val fileName: String, val bitmap: Bitmap)

    private companion object {
        private const val QR_SHARE_TITLE_MAX_LENGTH = 10
        private const val DEFAULT_FILE_NAME = "qrezzy_qr_code"
    }
}
