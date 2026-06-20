package software.mazur.qrezzy.feature.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import software.mazur.qrezzy.feature.scanner.components.ScannedQrDialog
import software.mazur.qrezzy.feature.scanner.components.ScannerPopup
import software.mazur.qrezzy.feature.scanner.components.ScannerPreview
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState.Mode

@Composable
fun ScannerScreen(viewModel: ScannerViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val qrSavedMessage = stringResource(R.string.scanner_qr_saved)
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) viewModel.onStartScanning() else viewModel.onPermissionDenied()
        }

    LaunchedEffect(Unit) {
        if (!context.hasCameraPermission()) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ScannerUiEvent.QrSaved ->
                    Toast.makeText(context, qrSavedMessage, Toast.LENGTH_SHORT).show()

                is ScannerUiEvent.ShowError ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    DisposableEffect(lifecycleOwner, uiState.mode) {
        val observer =
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if (uiState.mode == Mode.PermissionDenied && context.hasCameraPermission()) {
                            viewModel.onPermissionRestored()
                        }
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        if (uiState.mode == Mode.Scanning) {
                            viewModel.onStopScanning()
                        }
                    }

                    else -> Unit
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    uiState.detectedQr?.let { qr ->
        ScannedQrDialog(
            qr = qr,
            onSaveClick = viewModel::onSaveScannedQrClick,
            onCancelClick = viewModel::clearScannedQr,
        )
    }
    Column(modifier = Modifier.padding(horizontal = ScannerScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(title = stringResource(R.string.navigation_title_scan)) {
            QrezzyTopBarButton(
                onClick = viewModel::onTorchClick,
                enabled = uiState.isScanning,
                icon = if (uiState.isTorchEnabled) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                iconTint = if (uiState.isTorchEnabled) QrezzyPurpleDark else Color.Gray,
            )
        }

        Spacer(modifier = Modifier.height(ScannerScreenDefaults.topBarPreviewSpacing))

        ScannerPreview(
            modifier = Modifier.weight(ScannerScreenDefaults.PREVIEW_WEIGHT),
            isTorchEnabled = uiState.isTorchEnabled,
            isScanning = uiState.isScanning,
            onQrCodeScanned = viewModel::onQrCodeScanned,
        )

        Spacer(modifier = Modifier.height(ScannerScreenDefaults.previewButtonSpacing))

        QrezzyButton(
            onClick = {
                when (uiState.mode) {
                    Mode.Idle -> {
                        if (context.hasCameraPermission()) {
                            viewModel.onStartScanning()
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }

                    Mode.PermissionDenied -> {
                        context.openAppSettings()
                    }

                    Mode.Scanning -> {
                        viewModel.onStopScanning()
                    }
                }
            },
            text = stringResource(uiState.mode.getActionText()),
            elevation = 0.dp,
            containerColor = uiState.mode.actionContainerColor,
            depthColor = uiState.mode.actionDepthColor,
        )

        Spacer(modifier = Modifier.height(ScannerScreenDefaults.buttonPopupSpacing))

        ScannerPopup(isPermissionDenied = uiState.mode == Mode.PermissionDenied)

        Spacer(modifier = Modifier.height(ScannerScreenDefaults.bottomSpacing))
    }
}

private fun Mode.getActionText(): Int =
    when (this) {
        Mode.Idle -> R.string.scanner_action_scan
        Mode.Scanning -> R.string.scanner_action_stop
        Mode.PermissionDenied -> R.string.scanner_action_open_settings
    }

private val Mode.actionContainerColor
    get() =
        when (this) {
            Mode.Idle -> QrezzyMint
            Mode.PermissionDenied -> QrezzyYellow
            Mode.Scanning -> QrezzyPink
        }
private val Mode.actionDepthColor
    get() =
        when (this) {
            Mode.Idle -> QrezzyPurple
            Mode.PermissionDenied -> QrezzyMint
            Mode.Scanning -> QrezzyYellow
        }

private fun Context.hasCameraPermission(): Boolean {
    val permission = Manifest.permission.CAMERA
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

private fun Context.openAppSettings() {
    val action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val intent = Intent(action, Uri.fromParts("package", packageName, null))
    startActivity(intent)
}

private object ScannerScreenDefaults {
    val horizontalPadding = 16.dp
    val topBarPreviewSpacing = 16.dp
    val previewButtonSpacing = 20.dp
    val buttonPopupSpacing = 16.dp
    val bottomSpacing = 16.dp
    const val PREVIEW_WEIGHT = 1f
}
