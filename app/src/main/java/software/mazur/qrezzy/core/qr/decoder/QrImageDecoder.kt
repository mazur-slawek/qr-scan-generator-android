package software.mazur.qrezzy.core.qr.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class QrImageDecoder @Inject constructor(@param:ApplicationContext private val context: Context) {
    fun decode(uri: Uri): String? {
        val bitmap = decodeSampledBitmap(uri) ?: return null
        return decode(bitmap)
    }

    private fun decodeSampledBitmap(uri: Uri): Bitmap? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        inputStream.use { stream -> BitmapFactory.decodeStream(stream, null, bounds) }

        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val options = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, MAX_DECODE_DIMENSION)
        }
        return context.contentResolver
            .openInputStream(uri)
            ?.use { stream -> BitmapFactory.decodeStream(stream, null, options) }
    }

    /**
     * Converts bitmap pixels into a ZXing-compatible format and attempts to decode QR code content.
     * Returns null when no valid QR code can be found.
     */
    private fun decode(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()

        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val hints = mapOf(DecodeHintType.TRY_HARDER to true)

        return runCatching {
            MultiFormatReader().decode(binaryBitmap, hints).text
        }.getOrNull()
    }

    companion object {
        // Caps peak decode memory (an unsampled 12MP photo needs ~100MB: bitmap + ZXing pixel copy)
        // while keeping enough per-module resolution for ZXing to reliably read dense QR codes.
        private const val MAX_DECODE_DIMENSION = 1600

        internal fun calculateInSampleSize(width: Int, height: Int, maxDimension: Int): Int {
            var inSampleSize = 1
            var sampledWidth = width
            var sampledHeight = height

            while (maxOf(sampledWidth, sampledHeight) > maxDimension) {
                sampledWidth /= 2
                sampledHeight /= 2
                inSampleSize *= 2
            }

            return inSampleSize
        }
    }
}
