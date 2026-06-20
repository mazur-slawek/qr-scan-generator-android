package software.mazur.qrezzy.feature.history.details.model

import android.graphics.Bitmap
import software.mazur.qrezzy.domain.qr.model.Qr

data class HistoryDetailsUiState(
    val qr: Qr? = null,
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = true,
    val isMissing: Boolean = false,
)
