package software.mazur.qrezzy.feature.history.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiEvent
import software.mazur.qrezzy.feature.history.details.model.HistoryDetailsUiState

@Composable
fun HistoryDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryDetailsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val qrSharingService = rememberQrSharingService()
    val uiState by viewModel.uiState.collectAsState()
    val saveSuccessMessage = stringResource(R.string.history_details_save_success)
    val saveErrorMessage = stringResource(R.string.history_details_save_error)

    LaunchedEffect(windowInfo.containerSize.width, density) {
        val qrSizePx =
            with(density) {
                (windowInfo.containerSize.width - HistoryDetailsScreenDefaults.qrHorizontalOffset.roundToPx())
                    .coerceAtLeast(HistoryDetailsScreenDefaults.minQrSize.roundToPx())
            }
        viewModel.loadHistoryItem(qrSizePx)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HistoryDetailsUiEvent.ShareQrCode -> {
                    qrSharingService.shareBitmap(bitmap = event.bitmap, fileName = event.title)
                }

                is HistoryDetailsUiEvent.DownloadQrCode -> {
                    val saved = qrSharingService.saveBitmap(bitmap = event.bitmap, fileName = event.fileName)
                    Toast
                        .makeText(
                            context,
                            if (saved) saveSuccessMessage else saveErrorMessage,
                            Toast.LENGTH_SHORT,
                        ).show()
                }
            }
        }
    }

    Column(modifier = Modifier.padding(horizontal = HistoryDetailsScreenDefaults.screenHorizontalPadding)) {
        QrezzyTopBar(title = stringResource(R.string.history_details_title), onBackClick = onBackClick) {
            QrezzyTopBarButton(onClick = {}, icon = Icons.Outlined.FormatPaint, iconTint = QrezzyPurpleDark)
        }

        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.contentSpacing))

        HistoryQrPreviewCard(uiState = uiState, modifier = Modifier.weight(HistoryDetailsScreenDefaults.PREVIEW_WEIGHT))

        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.contentSpacing))

        QrezzyButton(
            elevation = 0.dp,
            containerColor = QrezzyMint,
            depthColor = QrezzyMintDark,
            onClick = viewModel::onShareQrCodeClick,
            text = stringResource(R.string.history_details_share),
        )

        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.buttonSpacing))

        QrezzyButton(
            elevation = 0.dp,
            containerColor = QrezzyPink,
            depthColor = QrezzyPinkDark,
            onClick = viewModel::onDownloadQrCodeClick,
            text = stringResource(R.string.history_details_download),
        )

        Spacer(modifier = Modifier.height(HistoryDetailsScreenDefaults.contentSpacing))
    }
}

@Composable
private fun HistoryQrPreviewCard(
    uiState: HistoryDetailsUiState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = HistoryDetailsScreenDefaults.cardElevation),
    ) {
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize()) {
            uiState.qrBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.history_details_qr_preview_content_description),
                )
            }
        }
    }
}

@Composable
private fun rememberQrSharingService(): QrSharingService {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        EntryPointAccessors
            .fromApplication(context = context, entryPoint = QrSharingEntryPoint::class.java)
            .qrSharingService()
    }
}

private object HistoryDetailsScreenDefaults {
    val screenHorizontalPadding = 16.dp
    val contentSpacing = 16.dp
    val buttonSpacing = 8.dp
    val qrHorizontalOffset = 100.dp
    val minQrSize = 240.dp
    val cardElevation = 2.dp
    const val PREVIEW_WEIGHT = 1f
}
