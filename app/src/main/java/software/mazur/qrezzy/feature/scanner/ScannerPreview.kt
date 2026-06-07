package software.mazur.qrezzy.feature.scanner

import android.widget.FrameLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import software.mazur.qrezzy.R

@Composable
fun ScannerPreview(isScanning: Boolean, modifier: Modifier = Modifier, isTorchEnabled: Boolean) {
    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = ScannerPreviewDefaults.HorizontalPadding)
            .clip(ScannerPreviewDefaults.ContainerShape)
            .background(ScannerPreviewDefaults.PlaceholderBackgroundColor)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isScanning) LiveCameraPreview(modifier = Modifier.matchParentSize(), isTorchEnabled = isTorchEnabled)

        ScannerFocusIndicator(
            isScanning = isScanning,
            modifier = Modifier.width(maxWidth * ScannerPreviewDefaults.FocusIndicatorWidthRatio),
        )
    }
}

@Composable
fun LiveCameraPreview(isTorchEnabled: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var camera by remember {mutableStateOf<Camera?>(null)}

    AndroidView(
        modifier = modifier,
        factory = {previewContext ->
            val container = FrameLayout(previewContext).apply {
                clipChildren = true
                clipToPadding = true
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
            val previewView = PreviewView(previewContext).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                )
            }
            container.addView(previewView)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(previewContext)
            cameraProviderFuture.addListener(
                {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()
                        .also {cameraPreview ->
                            cameraPreview.surfaceProvider = previewView.surfaceProvider
                        }

                    cameraProvider.unbindAll()

                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                    )
                },
                ContextCompat.getMainExecutor(previewContext),
            )

            container
        },
    )
    LaunchedEffect(camera, isTorchEnabled) {
        camera?.cameraControl?.enableTorch(isTorchEnabled)
    }
    DisposableEffect(Unit) {
        onDispose {
            camera?.cameraControl?.enableTorch(false)
            ProcessCameraProvider
                .getInstance(context)
                .get()
                .unbindAll()
        }
    }
}

@Composable
private fun ScannerFocusIndicator(isScanning: Boolean, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(if (isScanning) R.drawable.scanner_preview_on else R.drawable.scanner_preview_off),
        colorFilter = if (isScanning) null else ColorFilter.tint(ScannerPreviewDefaults.DisabledIndicatorTint),
        alpha = if (isScanning) 1f else 0.8f,
        contentDescription = null,
    )
}

private object ScannerPreviewDefaults {
    val HorizontalPadding = 16.dp
    val ContainerCornerRadius = 16.dp
    val ContainerShape = RoundedCornerShape(ContainerCornerRadius)
    val PlaceholderBackgroundColor = Color.LightGray
    val DisabledIndicatorTint = Color.White
    const val FocusIndicatorWidthRatio = 0.7f
}