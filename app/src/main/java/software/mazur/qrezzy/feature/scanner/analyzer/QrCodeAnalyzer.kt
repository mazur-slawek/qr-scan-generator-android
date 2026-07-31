package software.mazur.qrezzy.feature.scanner.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalyzer(private val onQrCodeScanned: (String) -> Unit) :
    ImageAnalysis.Analyzer,
    AutoCloseable {
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )
    private var isProcessing = false
    private var isClosed = false

    @OptIn(ExperimentalGetImage::class)
    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isClosed || isProcessing) {
            imageProxy.close()
            return
        }
        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val content = barcodes
                    .firstOrNull()
                    ?.rawValue
                    ?.trim()
                    .orEmpty()

                if (content.isNotBlank()) {
                    onQrCodeScanned(content)
                }
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }

    override fun close() {
        if (isClosed) return

        isClosed = true
        scanner.close()
    }
}
