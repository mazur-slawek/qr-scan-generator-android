package software.mazur.qrezzy.feature.history.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import software.mazur.qrezzy.core.qr.renderer.QrBitmapGenerator
import software.mazur.qrezzy.domain.qr.usecase.GetQrByIdUseCase
import software.mazur.qrezzy.feature.history.HistoryRoute
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiState
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getQrByIdUseCase: GetQrByIdUseCase,
        private val qrBitmapGenerator: QrBitmapGenerator,
    ) : ViewModel() {
        private val historyId: Long = checkNotNull(savedStateHandle[HistoryRoute.Details.HISTORY_ID_ARG])
        private val _uiState = MutableStateFlow(HistoryDetailsUiState())
        val uiState = _uiState.asStateFlow()
        private val _events = MutableSharedFlow<HistoryDetailsUiEvent>()
        val events = _events.asSharedFlow()
        private var loadJob: Job? = null
        private var lastQrSizePx: Int? = null

        fun loadHistoryItem(qrSizePx: Int) {
            if (shouldSkipLoading(qrSizePx)) return

            lastQrSizePx = qrSizePx
            loadJob?.cancel()

            loadJob =
                viewModelScope.launch {
                    val qr = getQrByIdUseCase(historyId)

                    if (qr == null) {
                        _uiState.update { it.copy(isLoading = false, isMissing = true) }
                        return@launch
                    }

                    _uiState.update { it.copy(qr = qr, isLoading = true, isMissing = false) }
                    val qrBitmap =
                        withContext(Dispatchers.Default) {
                            qrBitmapGenerator.generate(content = qr.content, size = qrSizePx)
                        }

                    _uiState.update {
                        it.copy(qrBitmap = qrBitmap, isLoading = false)
                    }
                }
        }

        private fun shouldSkipLoading(qrSizePx: Int): Boolean {
            val state = _uiState.value
            return lastQrSizePx == qrSizePx && state.qrBitmap != null && !state.isMissing
        }

        fun onShareQrCodeClick() {
            val state = _uiState.value
            val bitmap = state.qrBitmap ?: return
            val title = (state.qr ?: return).content.toQrFileName()
            viewModelScope.launch { _events.emit(HistoryDetailsUiEvent.ShareQrCode(title = title, bitmap = bitmap)) }
        }

        fun onDownloadQrCodeClick() {
            val state = _uiState.value
            val bitmap = state.qrBitmap ?: return
            val fileName = (state.qr ?: return).content.toQrFileName()
            viewModelScope.launch {
                _events.emit(HistoryDetailsUiEvent.DownloadQrCode(fileName = fileName, bitmap = bitmap))
            }
        }

        private fun String.toQrFileName(): String =
            trim()
                .take(QR_SHARE_TITLE_MAX_LENGTH)
                .replace(Regex("\\s+"), "_")
                .ifBlank { DEFAULT_FILE_NAME }

        private companion object {
            private const val QR_SHARE_TITLE_MAX_LENGTH = 10
            const val DEFAULT_FILE_NAME = "qrezzy_qr_code"
        }
    }
