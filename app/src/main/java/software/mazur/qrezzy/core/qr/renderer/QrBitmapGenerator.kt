package software.mazur.qrezzy.core.qr.renderer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.google.zxing.qrcode.encoder.Encoder
import java.util.EnumMap
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

class QrBitmapGenerator @Inject constructor() {
    fun generate(content: String, size: Int = DEFAULT_SIZE, style: QrStyle = QrStyle()): QrGenerationResult? {
        val trimmedContent = content.trim()
        if (trimmedContent.isBlank()) return null

        return try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
                put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARACTER_SET)
            }
            val qrCode = Encoder.encode(trimmedContent, style.errorCorrection.toZxingLevel(), hints)
            QrGenerationResult.Success(drawQrCode(matrix = qrCode.matrix, size = size, style = style))
        } catch (@Suppress("SwallowedException") exception: WriterException) {
            // Przyczyny WriterException nie da się niezawodnie rozróżnić bez parsowania message — oczekiwany wynik, nie błąd.
            QrGenerationResult.CannotEncode
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            QrGenerationResult.Failed(exception)
        }
    }

    @SuppressLint("UseKtx")
    private fun drawQrCode(matrix: ByteMatrix, size: Int, style: QrStyle): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(style.backgroundColor.toAndroidColor())
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.qrColor.toAndroidColor()
            this.style = Paint.Style.FILL
        }
        val matrixWidth = matrix.width
        val matrixHeight = matrix.height
        val qrSizeWithMargin = maxOf(matrixWidth, matrixHeight) + DEFAULT_MARGIN * 2
        val moduleSize = size / qrSizeWithMargin.toFloat()
        val qrPixelSize = matrixWidth * moduleSize
        val startX = (size - qrPixelSize) / 2f
        val startY = (size - qrPixelSize) / 2f

        for (x in 0 until matrixWidth) {
            for (y in 0 until matrixHeight) {
                if (matrix.get(x, y).toInt() == 1) {
                    canvas.drawQrModule(
                        left = startX + x * moduleSize,
                        top = startY + y * moduleSize,
                        size = moduleSize,
                        patternStyle = style.patternStyle,
                        paint = paint
                    )
                }
            }
        }
        return bitmap
    }

    private fun Canvas.drawQrModule(left: Float, top: Float, size: Float, patternStyle: QrPatternStyle, paint: Paint) {
        when (patternStyle) {
            QrPatternStyle.SQUARE -> {
                drawRect(left, top, left + size, top + size, paint)
            }

            QrPatternStyle.DOTS -> {
                drawCircle(left + size / 2f, top + size / 2f, size * DOT_RADIUS_FACTOR, paint)
            }

            QrPatternStyle.ROUNDED -> {
                val inset = size * ROUNDED_INSET_FACTOR
                val radius = size * ROUNDED_RADIUS_FACTOR

                drawRoundRect(
                    RectF(left + inset, top + inset, left + size - inset, top + size - inset),
                    radius,
                    radius,
                    paint
                )
            }
        }
    }

    private fun Long.toAndroidColor(): Int = toInt()

    private companion object {
        const val DEFAULT_SIZE = 512
        const val DEFAULT_MARGIN = 2
        const val DEFAULT_CHARACTER_SET = "UTF-8"
        const val DOT_RADIUS_FACTOR = 0.38f
        const val ROUNDED_INSET_FACTOR = 0.08f
        const val ROUNDED_RADIUS_FACTOR = 0.35f
    }
}
