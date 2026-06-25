package software.mazur.qrezzy.feature.history.details.model

import android.graphics.Bitmap
import software.mazur.qrezzy.core.qr.style.QrStyleEditorState
import software.mazur.qrezzy.domain.qr.model.Qr

data class HistoryDetailsUiState(
    val qr: Qr? = null,
    val qrBitmap: Bitmap? = null,
    val editPreviewBitmap: Bitmap? = null,
    val qrStyleEditor: QrStyleEditorState = QrStyleEditorState(),
    val isLoading: Boolean = true,
    val isMissing: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false
)