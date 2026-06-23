package software.mazur.qrezzy.feature.history.details

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.usecase.DeleteQrItemsUseCase
import software.mazur.qrezzy.domain.qr.usecase.GetQrByIdUseCase
import software.mazur.qrezzy.feature.history.HistoryRoute
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiState
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQrByIdUseCase: GetQrByIdUseCase,
    private val qrBitmapGenerator: QrBitmapGenerator,
    private val deleteQrItemsUseCase: DeleteQrItemsUseCase
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

    private suspend fun loadQr() {
        val qr = getQrByIdUseCase(historyId)

        if (qr == null) {
            _uiState.update { it.copy(isLoading = false, isMissing = true) }
            return
        }

        _uiState.update { it.copy(qr = qr, isLoading = true, isMissing = false) }
        val qrBitmap = withContext(Dispatchers.Default) {
            qrBitmapGenerator.generate(content = qr.content)
        }

        _uiState.update { it.copy(qrBitmap = qrBitmap, isLoading = false) }
    }

    private fun getShareData(): QrShareData? {
        val state = _uiState.value
        val qr = state.qr ?: return null
        val bitmap = state.qrBitmap ?: return null
        return QrShareData(fileName = qr.content.toQrFileName(), bitmap = bitmap)
    }

    private fun String.toQrFileName(): String {
        return trim()
            .take(QR_SHARE_TITLE_MAX_LENGTH)
            .replace(Regex("\\s+"), "_")
            .ifBlank { DEFAULT_FILE_NAME }
    }

    private data class QrShareData(
        val fileName: String,
        val bitmap: Bitmap,
    )

    private companion object {
        private const val QR_SHARE_TITLE_MAX_LENGTH = 10
        private const val DEFAULT_FILE_NAME = "qrezzy_qr_code"
    }
}