package software.mazur.qrezzy.feature.generator.domain

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import javax.inject.Inject

class GenerateQrBitmapUseCase @Inject constructor() {
    operator fun invoke(content: String, size: Int = DEFAULT_SIZE): Bitmap? {
        if (content.isBlank()) return null
        val bitMatrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
        )
        @SuppressLint("UseKtx")
        return Bitmap.createBitmap(
            size,
            size,
            Bitmap.Config.ARGB_8888
        ).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val color = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    setPixel(x, y, color)
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_SIZE = 700
    }
}