package software.mazur.qrezzy.feature.generator

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun GeneratorScreen() {
    val qrBitmap =
        remember {
            generateQrBitmap(
                content = "https://github.com/mazur-slawek/qr-scan-generator-android",
            )
        }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            bitmap = qrBitmap.asImageBitmap(),
            contentDescription = "QR Code",
        )
    }
}

fun generateQrBitmap(
    content: String,
    size: Int = 800,
): Bitmap {
    val bitMatrix =
        QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
        )

    return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
        for (x in 0 until size) {
            for (y in 0 until size) {
                setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    },
                )
            }
        }
    }
}
