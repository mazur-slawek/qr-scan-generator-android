package software.mazur.qrezzy.core.qr.renderer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.EnumMap
import javax.inject.Inject

class QrBitmapGenerator
@Inject
constructor() {
    fun generate(content: String, size: Int = DEFAULT_SIZE): Bitmap? {
        val trimmedContent = content.trim()

        if (trimmedContent.isBlank()) return null
        val hints =
            EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
                put(EncodeHintType.MARGIN, DEFAULT_MARGIN)
                put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARACTER_SET)
            }
        val bitMatrix =
            QRCodeWriter().encode(
                trimmedContent,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints,
            )

        @SuppressLint("UseKtx")
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    setPixel(x, y, color)
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_SIZE = 512
        const val DEFAULT_MARGIN = 1
        const val DEFAULT_CHARACTER_SET = "UTF-8"
    }
}
