package software.mazur.qrezzy.core.qr.renderer

import android.graphics.Bitmap

sealed interface QrGenerationResult {
    data class Success(val bitmap: Bitmap) : QrGenerationResult
    data object CannotEncode : QrGenerationResult
    data class Failed(val throwable: Throwable) : QrGenerationResult
}
