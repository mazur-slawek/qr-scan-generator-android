package software.mazur.qrezzy.feature.history.details.model

import android.graphics.Bitmap

data class HistoryDetailsUiState(
    val title: String = "",
    val content: String = "",
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = true,
    val isMissing: Boolean = false
)