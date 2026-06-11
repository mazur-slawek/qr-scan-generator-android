package software.mazur.qrezzy.feature.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow


private enum class ScannerScreenState {
    Idle,
    Scanning,
    PermissionDenied,
}

@Composable
fun ScannerScreen() {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    var scannerState by remember {
        mutableStateOf(ScannerScreenState.Idle)
    }
    var isTorchEnabled by remember {
        mutableStateOf(false)
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        scannerState = if (isGranted) ScannerScreenState.Scanning else ScannerScreenState.PermissionDenied
    }
    LaunchedEffect(Unit) {
        if (!context.hasCameraPermission()) {
            cameraPermissionLauncher.launch(cameraPermission)
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, scannerState) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (
                        scannerState == ScannerScreenState.PermissionDenied &&
                        context.hasCameraPermission()
                    ) {
                        scannerState = ScannerScreenState.Idle
                    }
                }

                Lifecycle.Event.ON_PAUSE  -> {
                    if (scannerState == ScannerScreenState.Scanning) {
                        scannerState = ScannerScreenState.Idle
                        isTorchEnabled = false
                    }
                }

                else                      -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        QrezzyTopBar(title = stringResource(R.string.navigation_title_scan)) {
            QrezzyTopBarButton(
                onClick = { isTorchEnabled = !isTorchEnabled },
                enabled = scannerState == ScannerScreenState.Scanning,
                icon = if (isTorchEnabled) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                iconTint = if (isTorchEnabled) QrezzyPurpleDark else Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ScannerPreview(
            modifier = Modifier.weight(1f),
            isTorchEnabled = isTorchEnabled,
            isScanning = scannerState == ScannerScreenState.Scanning,
        )
        Spacer(modifier = Modifier.height(20.dp))
        QrezzyButton(
            onClick = {
                when (scannerState) {
                    ScannerScreenState.Idle             -> {
                        if (context.hasCameraPermission()) {
                            scannerState = ScannerScreenState.Scanning
                        } else {
                            cameraPermissionLauncher.launch(cameraPermission)
                        }
                    }

                    ScannerScreenState.PermissionDenied -> {
                        context.openAppSettings()
                    }

                    ScannerScreenState.Scanning         -> {
                        scannerState = ScannerScreenState.Idle
                        isTorchEnabled = false
                    }
                }
            },
            text = stringResource(scannerState.getActionText()),
            elevation = 0.dp,
            containerColor = when (scannerState) {
                ScannerScreenState.Idle             -> QrezzyMint
                ScannerScreenState.PermissionDenied -> QrezzyYellow
                ScannerScreenState.Scanning         -> QrezzyPink
            },
            depthColor = when (scannerState) {
                ScannerScreenState.Idle             -> QrezzyPurple
                ScannerScreenState.PermissionDenied -> QrezzyMint
                ScannerScreenState.Scanning         -> QrezzyYellow
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ScannerPopup(isPermissionDenied = scannerState == ScannerScreenState.PermissionDenied)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun ScannerScreenState.getActionText(): Int {
    return when (this) {
        ScannerScreenState.Idle             -> R.string.scanner_action_scan
        ScannerScreenState.Scanning         -> R.string.scanner_action_stop
        ScannerScreenState.PermissionDenied -> R.string.scanner_action_open_settings
    }
}

private fun android.content.Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun android.content.Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    )
    startActivity(intent)
}