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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyHistoryLimitPopup
import software.mazur.qrezzy.core.designsystem.components.QrezzyPopup
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.feature.scanner.components.ScannedQrDialog
import software.mazur.qrezzy.feature.scanner.components.ScannerPreview
import software.mazur.qrezzy.feature.scanner.model.ScannerUiEvent
import software.mazur.qrezzy.feature.scanner.model.ScannerUiState.Mode

@Composable
fun ScannerScreen(viewModel: ScannerViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val qrSavedMessage = stringResource(R.string.scanner_qr_saved)
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) viewModel.onStartScanning() else viewModel.onPermissionDenied()
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let(viewModel::onImageSelected)
    }
    val popupContent = ScannerPopupContent.resolve(isPermissionDenied = uiState.mode == Mode.PermissionDenied)

    LaunchedEffect(Unit) {
        viewModel.onScreenOpened()
        if (!context.hasCameraPermission()) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ScannerUiEvent.QrSaved      ->
                    Toast.makeText(context, qrSavedMessage, Toast.LENGTH_SHORT).show()

                is ScannerUiEvent.ShowError ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME                         ->
                    if (context.hasCameraPermission()) viewModel.onPermissionRestored()

                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> viewModel.onStopScanning()
                else                                              -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            viewModel.onStopScanning()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    uiState.detectedQr?.let { qr ->
        ScannedQrDialog(
            qr = qr,
            showSaveButton = !uiState.autoSaveScans,
            isSaveEnabled = !uiState.isSaveBlockedByHistoryLimit,
            onSaveClick = viewModel::onSaveScannedQrClick,
            onCancelClick = viewModel::clearScannedQr
        )
    }

    Column(modifier = Modifier.padding(horizontal = ScannerScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(titleResId = R.string.navigation_title_scan, subtitleResId = R.string.navigation_subtitle_scan) {
            QrezzyTopBarButton(
                onClick = viewModel::onTorchClick,
                enabled = uiState.isScanning,
                iconPainter = painterResource(
                    if (uiState.isTorchEnabled) R.drawable.qrezzy_torch_on else R.drawable.qrezzy_torch_off),
            )
        }
        Spacer(modifier = Modifier.height(ScannerScreenDefaults.topBarPreviewSpacing))
        ScannerPreview(
            modifier = Modifier.weight(ScannerScreenDefaults.PREVIEW_WEIGHT),
            isTorchEnabled = uiState.isTorchEnabled,
            isScanning = uiState.isScanning,
            onQrCodeScanned = viewModel::onQrCodeScanned,
        )
        if (uiState.showHistoryLimitReachedPopup) {
            Spacer(modifier = Modifier.height(ScannerScreenDefaults.previewButtonSpacing))
            QrezzyHistoryLimitPopup()
        }
        if (uiState.mode == Mode.Idle) {
            Spacer(modifier = Modifier.height(ScannerScreenDefaults.previewButtonSpacing))
            QrezzyButton(
                onClick = {
                    if (context.hasCameraPermission()) {
                        viewModel.onStartScanning()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                text = stringResource(R.string.scanner_action_scan),
                elevation = 0.dp,
            )
        } else if (uiState.mode == Mode.Scanning) {
            Spacer(modifier = Modifier.height(ScannerScreenDefaults.previewButtonSpacing))
            QrezzyButton(
                onClick = { viewModel.onStopScanning() },
                text = stringResource(R.string.scanner_action_stop),
                containerColor = QrezzyPink,
                depthColor = QrezzyYellow,
                elevation = 0.dp,
            )
        }
        Spacer(modifier = Modifier.height(ScannerScreenDefaults.buttonsSpacing))
        QrezzyButton(
            onClick = { imagePickerLauncher.launch("image/*") },
            elevation = 0.dp,
            containerColor = QrezzyYellow,
            depthColor = QrezzyPurple,
            text = stringResource(R.string.scanner_action_select_image)
        )
        Spacer(modifier = Modifier.height(ScannerScreenDefaults.buttonPopupSpacing))
        QrezzyPopup(
            imageResId = popupContent.imageResId,
            titleResId = popupContent.titleResId,
            descriptionResId = popupContent.descriptionResId
        )
        if (uiState.mode == Mode.PermissionDenied) {
            Spacer(modifier = Modifier.height(ScannerScreenDefaults.buttonPopupSpacing))
            QrezzyButton(
                onClick = { context.openAppSettings() },
                text = stringResource(R.string.scanner_action_open_settings),
                containerColor = QrezzyYellow,
                depthColor = QrezzyMint,
                elevation = 0.dp,
            )
        }
        Spacer(modifier = Modifier.height(ScannerScreenDefaults.bottomSpacing))
    }
}

@Immutable
private data class ScannerPopupContent(val imageResId: Int, val titleResId: Int, val descriptionResId: Int) {
    companion object {
        fun resolve(isPermissionDenied: Boolean): ScannerPopupContent =
            if (isPermissionDenied) {
                ScannerPopupContent(
                    imageResId = R.drawable.qrezzy_mascot_camera_permission_denied,
                    titleResId = R.string.scanner_popup_permission_denied_title,
                    descriptionResId = R.string.scanner_popup_permission_denied_desc,
                )
            } else {
                ScannerPopupContent(
                    imageResId = R.drawable.qrezzy_mascot_scanner,
                    titleResId = R.string.scanner_popup_idle_title,
                    descriptionResId = R.string.scanner_popup_idle_desc,
                )
            }
    }
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
    val buttonsSpacing = 10.dp
    val bottomSpacing = 16.dp
    const val PREVIEW_WEIGHT = 1f
}
