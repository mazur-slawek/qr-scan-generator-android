package software.mazur.qrezzy.feature.generator.model

import android.graphics.Bitmap

sealed interface QrPreviewState {
    data object Idle : QrPreviewState
    data class Generating(val staleBitmap: Bitmap? = null) : QrPreviewState
    data class Ready(val bitmap: Bitmap) : QrPreviewState
    data class Error(val error: QrGenerationError) : QrPreviewState
}

internal fun QrPreviewState.toPendingState(content: String): QrPreviewState {
    if (content.isBlank()) return QrPreviewState.Idle

    val staleBitmap = when (this) {
        is QrPreviewState.Ready -> bitmap
        is QrPreviewState.Generating -> staleBitmap
        QrPreviewState.Idle, is QrPreviewState.Error -> null
    }
    return QrPreviewState.Generating(staleBitmap = staleBitmap)
}
