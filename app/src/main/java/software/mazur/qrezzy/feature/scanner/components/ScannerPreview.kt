package software.mazur.qrezzy.feature.scanner.components

import android.graphics.Color
import android.widget.FrameLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.atomic.AtomicBoolean
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.feature.scanner.analyzer.QrCodeAnalyzer

@Composable
fun ScannerPreview(isScanning: Boolean, isTorchEnabled: Boolean, onQrCodeScanned: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = ScannerPreviewDefaults.Container.elevation)
    ) {
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isScanning) {
                    LiveCameraPreview(
                        modifier = Modifier.matchParentSize(),
                        onQrCodeScanned = onQrCodeScanned,
                        isTorchEnabled = isTorchEnabled
                    )
                }
                ScannerFocusIndicator(
                    modifier = Modifier.width(maxWidth * ScannerPreviewDefaults.FOCUS_INDICATOR_WIDTH_RATIO)
                )
            }
        }
    }
}

@Composable
fun LiveCameraPreview(isTorchEnabled: Boolean, onQrCodeScanned: (String) -> Unit, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnQrCodeScanned by rememberUpdatedState(onQrCodeScanned)
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }
    var cameraProvider by remember {
        mutableStateOf<ProcessCameraProvider?>(null)
    }
    var previewUseCase by remember {
        mutableStateOf<Preview?>(null)
    }
    var imageAnalysisUseCase by remember {
        mutableStateOf<ImageAnalysis?>(null)
    }
    var qrCodeAnalyzer by remember {
        mutableStateOf<QrCodeAnalyzer?>(null)
    }
    val isDisposed = remember {
        AtomicBoolean(false)
    }
    AndroidView(
        modifier = modifier,
        factory = { previewContext ->
            val container = FrameLayout(previewContext).apply {
                clipChildren = true
                clipToPadding = true
                setBackgroundColor(Color.TRANSPARENT)
            }
            val previewView = PreviewView(previewContext).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                setBackgroundColor(Color.TRANSPARENT)

                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

            container.addView(previewView)
            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(previewContext)

            cameraProviderFuture.addListener(
                {
                    if (isDisposed.get()) {
                        return@addListener
                    }
                    val provider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()
                        .also { cameraPreview ->
                            cameraPreview.surfaceProvider =
                                previewView.surfaceProvider
                        }
                    val analyzer = QrCodeAnalyzer { content ->
                        currentOnQrCodeScanned(content)
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(
                            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                        )
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(
                                ContextCompat.getMainExecutor(previewContext),
                                analyzer
                            )
                        }

                    cameraProvider = provider
                    previewUseCase = preview
                    imageAnalysisUseCase = imageAnalysis
                    qrCodeAnalyzer = analyzer

                    camera = runCatching {
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    }.getOrNull()
                },
                ContextCompat.getMainExecutor(previewContext)
            )

            container
        }
    )

    LaunchedEffect(camera, isTorchEnabled) {
        camera
            ?.cameraControl
            ?.enableTorch(isTorchEnabled)
    }

    DisposableEffect(lifecycleOwner) {
        isDisposed.set(false)

        onDispose {
            isDisposed.set(true)

            camera
                ?.cameraControl
                ?.enableTorch(false)

            imageAnalysisUseCase?.clearAnalyzer()
            val provider = cameraProvider
            val preview = previewUseCase
            val imageAnalysis = imageAnalysisUseCase

            if (provider != null && preview != null && imageAnalysis != null) {
                provider.unbind(preview, imageAnalysis)
            }

            qrCodeAnalyzer?.close()

            camera = null
            cameraProvider = null
            previewUseCase = null
            imageAnalysisUseCase = null
            qrCodeAnalyzer = null
        }
    }
}

@Composable
private fun ScannerFocusIndicator(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        contentDescription = null,
        painter = painterResource(R.drawable.qr_frame)
    )
}

private object ScannerPreviewDefaults {
    object Container {
        val elevation = 2.dp
    }

    const val FOCUS_INDICATOR_WIDTH_RATIO = 0.7f
}
