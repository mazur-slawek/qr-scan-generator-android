package software.mazur.qrezzy.feature.history.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.EntryPointAccessors
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.common.di.QrSharingEntryPoint
import software.mazur.qrezzy.core.common.sharing.QrSharingService
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyCustomizeQrDialog
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyQrPreview
import software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails.QrezzyQrInfo
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.history.components.DeleteQrConfirmationDialog
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent

@Composable
fun HistoryDetailsScreen(onBackClick: () -> Unit, viewModel: HistoryDetailsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val qrSharingService = rememberQrSharingService()
    val uiState by viewModel.uiState.collectAsState()
    val saveSuccessMessage = stringResource(R.string.history_details_save_success)
    val saveErrorMessage = stringResource(R.string.history_details_save_error)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HistoryDetailsUiEvent.ShareQrCode    -> {
                    qrSharingService.shareBitmap(bitmap = event.bitmap, fileName = event.title)
                }

                is HistoryDetailsUiEvent.DownloadQrCode -> {
                    val isSaved = qrSharingService.saveBitmap(bitmap = event.bitmap, fileName = event.fileName)

                    Toast.makeText(
                        context,
                        if (isSaved) saveSuccessMessage else saveErrorMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                is HistoryDetailsUiEvent.OnBack         -> {
                    onBackClick()
                }
            }
        }
    }

    if (uiState.isDeleteConfirmationVisible) {
        DeleteQrConfirmationDialog(
            onCancelClick = viewModel::onDeleteConfirmationDialogDismiss,
            onConfirmClick = viewModel::onDeleteConfirmationDialogConfirm
        )
    }

    if (uiState.qrStyleEditor.isDialogVisible) {
        QrezzyCustomizeQrDialog(
            qrBitmap = uiState.editPreviewBitmap,
            style = uiState.qrStyleEditor.draftStyle,
            isEditMode = true,
            onQrColorSelected = viewModel::onQrColorSelected,
            onBackgroundColorSelected = viewModel::onBackgroundColorSelected,
            onPatternStyleSelected = viewModel::onPatternStyleSelected,
            onErrorCorrectionSelected = viewModel::onErrorCorrectionSelected,
            onCancelClick = viewModel::onDismissCustomizeQrDialog,
            onResetClick = viewModel::onResetQrStyleClick,
            onApplyClick = viewModel::onSaveQrStyleClick
        )
    }

    Column(modifier = Modifier.padding(horizontal = HistoryDetailsScreenDefaults.screenHorizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.history_details_title,
            subtitleResId = R.string.history_details_subtitle,
            onBackClick = onBackClick
        ) {
            QrezzyTopBarButton(
                iconPainter = painterResource(if (uiState.qr?.isFavorite == true) {
                    R.drawable.qrezzy_favorite_on
                } else {
                    R.drawable.qrezzy_favorite_off
                }),
                onClick = viewModel::onFavoriteClick,
                iconTint = QrezzyYellowDark
            )
            Spacer(modifier = Modifier.width(10.dp))
            QrezzyTopBarButton(
                iconPainter = painterResource(R.drawable.qrezzy_edit),
                onClick = viewModel::onCustomizeQrClick,
            )
        }
        LazyColumn {
            item { QrezzyQrPreview(qrBitmap = uiState.qrBitmap) }
            uiState.qr?.let { qr -> item { QrezzyQrInfo(qr = qr) } }
            item {
                HistoryDetailsActions(
                    onShareClick = viewModel::onShareQrCodeClick,
                    onDownloadClick = viewModel::onDownloadQrCodeClick,
                    onDeleteClick = viewModel::onDeleteQrCodeClick,
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailsActions(onShareClick: () -> Unit, onDownloadClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing * 2))
        QrezzyButton(
            elevation = 0.dp,
            onClick = onShareClick,
            text = stringResource(R.string.history_details_share),
        )
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing))
        QrezzyButton(
            elevation = 0.dp,
            onClick = onDownloadClick,
            text = stringResource(R.string.history_details_download),
        )
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing * 2))
        QrezzyButton(
            elevation = 0.dp,
            containerColor = QrezzyPink,
            depthColor = QrezzyYellowDark,
            onClick = onDeleteClick,
            text = stringResource(R.string.history_details_delete),
        )
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing * 2))
    }
}

@Composable
private fun rememberQrSharingService(): QrSharingService {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        EntryPointAccessors.fromApplication(context, QrSharingEntryPoint::class.java).qrSharingService()
    }
}

private object HistoryDetailsScreenDefaults {
    val screenHorizontalPadding = 16.dp
    val buttonSpacing = 8.dp
}