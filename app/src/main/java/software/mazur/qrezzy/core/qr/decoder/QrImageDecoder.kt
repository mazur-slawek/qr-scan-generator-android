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
        val bitmap = context.contentResolver
            .openInputStream(uri)
            ?.use { inputStream -> BitmapFactory.decodeStream(inputStream) } ?: return null

        return decode(bitmap)
    }

    /**
     * Converts bitmap pixels into a ZXing-compatible format and attempts to decode QR code content.
     * Returns null when no valid QR code can be found.
     */
    private fun decode(bitmap: Bitmap): String? {
        val pixels = IntArray(bitmap.width * bitmap.height)

        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val hints = mapOf(DecodeHintType.TRY_HARDER to true)

        return runCatching {
            MultiFormatReader().decode(binaryBitmap, hints).text
        }.getOrNull()
    }
}