package software.mazur.qrezzy.feature.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.Purple

@Composable
fun ScannerScreen() {
    val isTorchEnabled = remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        testMlKitBarcodeScanner()
    }
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) {isGranted ->
            hasCameraPermission = isGranted
        }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column {
        QrezzyTopBar(
            title = stringResource(R.string.navigation_title_scan),
            rightButton = QrezzyTopBarButton(
                icon = if (isTorchEnabled.value) {
                    Icons.Outlined.FlashOn
                } else {
                    Icons.Outlined.FlashOff
                },
                iconTint = if (isTorchEnabled.value) {
                    Purple
                } else {
                    Color.Gray
                },
                onClick = {isTorchEnabled.value = !isTorchEnabled.value}
            ),
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (hasCameraPermission) {
                CameraPreview()
            } else {
                CameraPermissionContent()
            }
        }
    }
}

@Composable
private fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    AndroidView(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),
        factory = {previewContext ->
            val previewView = PreviewView(previewContext)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(previewContext)

            cameraProviderFuture.addListener(
                {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview =
                        Preview
                            .Builder()
                            .build()
                            .also {cameraPreview ->
                                cameraPreview.surfaceProvider = previewView.surfaceProvider
                            }

                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                    )
                },
                ContextCompat.getMainExecutor(context),
            )

            previewView
        },
    )

    DisposableEffect(Unit) {
        onDispose {
            ProcessCameraProvider.getInstance(context).get().unbindAll()
        }
    }
}

@Composable
private fun CameraPermissionContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Camera permission is required to scan QR codes.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun testMlKitBarcodeScanner() {
    val scanner = BarcodeScanning.getClient()
    Log.d("QREZZY_ML_KIT", "ML Kit scanner created: $scanner")
}
