package software.mazur.qrezzy.feature.history.details

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.EntryPointAccessors
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.common.di.QrSharingEntryPoint
import software.mazur.qrezzy.core.common.sharing.QrSharingService
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails.QrezzyQrInfo
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
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

    Column(modifier = Modifier.padding(horizontal = HistoryDetailsScreenDefaults.screenHorizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.history_details_title,
            subtitleResId = R.string.history_details_subtitle,
            onBackClick = onBackClick
        ) {
            IconButton(onClick = viewModel::onFavoriteClick) {
                Icon(
                    imageVector = if (uiState.qr?.isFavorite == true) Icons.Default.Star else Icons.Default.StarBorder,
                    tint = if (uiState.qr?.isFavorite == true) QrezzyYellowDark else TextSecondary,
                    modifier = Modifier.size(HistoryDetailsScreenDefaults.favoriteIconSize),
                    contentDescription = null,
                )
            }
        }
        LazyColumn {
            item { Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.sectionSpacing)) }
            item { HistoryDetailsQrPreview(bitmap = uiState.qrBitmap) }
            item { Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.sectionSpacing)) }
            uiState.qr?.let { qr -> item { QrezzyQrInfo(qr = qr) } }
            item { Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.sectionSpacing)) }
            item {
                HistoryDetailsActions(
                    onShareClick = viewModel::onShareQrCodeClick,
                    onDownloadClick = viewModel::onDownloadQrCodeClick,
                    onDeleteClick = viewModel::onDeleteQrCodeClick,
                )
            }
            item { Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.sectionSpacing)) }
        }
    }
}

@Composable
private fun HistoryDetailsQrPreview(bitmap: Bitmap?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(HistoryDetailsScreenDefaults.qrPreviewHeight),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = HistoryDetailsScreenDefaults.cardElevation),
    ) {
        QrezzyAnimatedStars {
            bitmap?.let { qrBitmap ->
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.history_details_qr_preview_content_description)
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailsActions(
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing))
        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing))
        QrezzyButton(
            elevation = 0.dp,
            containerColor = QrezzyPink,
            depthColor = QrezzyYellowDark,
            onClick = onDeleteClick,
            text = stringResource(R.string.history_details_delete),
        )
    }
}

@Composable
private fun rememberQrSharingService(): QrSharingService {
    val context = LocalContext.current.applicationContext

    return remember(context) {
        EntryPointAccessors
            .fromApplication(context, QrSharingEntryPoint::class.java)
            .qrSharingService()
    }
}

private object HistoryDetailsScreenDefaults {
    val screenHorizontalPadding = 16.dp
    val sectionSpacing = 16.dp
    val buttonSpacing = 8.dp
    val qrPreviewHeight = 250.dp
    val cardElevation = 2.dp
    val favoriteIconSize = 32.dp
}