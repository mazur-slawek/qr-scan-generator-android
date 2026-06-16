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
import software.mazur.qrezzy.domain.history.usecase.GetQrByIdUseCase
import software.mazur.qrezzy.feature.generator.domain.GenerateQrBitmapUseCase
import software.mazur.qrezzy.feature.history.HistoryRoute
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiState
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQrHistoryByIdUseCase: GetQrByIdUseCase,
    private val generateQrBitmapUseCase: GenerateQrBitmapUseCase,
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

        loadJob = viewModelScope.launch {
            val item = getQrHistoryByIdUseCase(historyId)

            if (item == null) {
                _uiState.update { it.copy(isLoading = false, isMissing = true) }
                return@launch
            }

            _uiState.update {
                it.copy(
                    title = item.title,
                    content = item.content,
                    isLoading = true,
                    isMissing = false,
                )
            }
            val qrBitmap = withContext(Dispatchers.Default) {
                generateQrBitmapUseCase(content = item.content, size = qrSizePx)
            }

            _uiState.update {
                it.copy(qrBitmap = qrBitmap, isLoading = false)
            }
        }
    }

    fun onShareQrCodeClick() {
        val state = _uiState.value
        val bitmap = state.qrBitmap ?: return

        viewModelScope.launch {
            _events.emit(HistoryDetailsUiEvent.ShareQrCode(title = state.title, bitmap = bitmap))
        }
    }

    fun onDownloadQrCodeClick() {
        val state = _uiState.value
        val bitmap = state.qrBitmap ?: return

        viewModelScope.launch {
            _events.emit(HistoryDetailsUiEvent.DownloadQrCode(
                fileName = state.title.ifBlank { DEFAULT_FILE_NAME },
                bitmap = bitmap,
            ))
        }
    }

    private fun shouldSkipLoading(qrSizePx: Int): Boolean {
        val state = _uiState.value
        return lastQrSizePx == qrSizePx && state.qrBitmap != null && !state.isMissing
    }

    private companion object {
        const val DEFAULT_FILE_NAME = "qrezzy_qr_code"
    }
}