package software.mazur.qrezzy.feature.history.details.model

import android.graphics.Bitmap

sealed interface HistoryDetailsUiEvent {
    data class ShareQrCode(
        val title: String,
        val bitmap: Bitmap,
    ) : HistoryDetailsUiEvent

    data class DownloadQrCode(
        val fileName: String,
        val bitmap: Bitmap,
    ) : HistoryDetailsUiEvent
    
    data object QrStyleSaved : HistoryDetailsUiEvent
    data object QrStyleSaveFailed : HistoryDetailsUiEvent
    object OnBack : HistoryDetailsUiEvent
}
