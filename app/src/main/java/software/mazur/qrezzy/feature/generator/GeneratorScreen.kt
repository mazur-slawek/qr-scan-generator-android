package software.mazur.qrezzy.feature.generator

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyCustomizeQrDialog
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyQrPreview
import software.mazur.qrezzy.feature.generator.components.QrTypeForm
import software.mazur.qrezzy.feature.generator.components.QrTypeTabs
import software.mazur.qrezzy.feature.generator.model.GeneratorUiEvent
import software.mazur.qrezzy.feature.generator.model.GeneratorUiState

@Composable
fun GeneratorScreen(viewModel: GeneratorViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val qrSavedMessage = stringResource(R.string.generator_qr_saved_message)
    val qrSaveFailedMessage = stringResource(R.string.generator_qr_save_failed_message)
    val qrBitmap = remember(uiState.qrContent, uiState.qrStyle) {
        viewModel.generateQrBitmap(content = uiState.qrContent)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GeneratorUiEvent.QrSaved      -> {
                    Toast.makeText(context, qrSavedMessage, Toast.LENGTH_SHORT).show()
                }

                GeneratorUiEvent.QrSaveFailed -> {
                    Toast.makeText(context, qrSaveFailedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    GeneratorCustomizeQrDialog(uiState = uiState, viewModel = viewModel)
    
    Column(modifier = Modifier.padding(horizontal = GeneratorScreenDefaults.screenHorizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_generate,
            subtitleResId = R.string.navigation_subtitle_generate,
        ) {
            QrezzyTopBarButton(
                enabled = uiState.canSave,
                iconPainter = painterResource(R.drawable.qrezzy_edit),
                onClick = viewModel::onCustomizeQrClick,
            )
        }
        QrezzyQrPreview(qrBitmap = qrBitmap)
        GeneratorContent(
            uiState = uiState,
            onQrInputSelected = viewModel::onQrInputSelected,
            onFormEvent = viewModel::onFormEvent,
            onSaveClick = viewModel::saveQrCode,
            onClearFocus = focusManager::clearFocus,
        )
    }
}

@Composable
private fun GeneratorCustomizeQrDialog(uiState: GeneratorUiState, viewModel: GeneratorViewModel) {
    if (!uiState.qrStyleEditor.isDialogVisible) return
    val previewBitmap = remember(uiState.qrContent, uiState.qrStyleEditor.draftStyle) {
        viewModel.generatePreviewQrBitmap(content = uiState.qrContent, style = uiState.qrStyleEditor.draftStyle)
    }

    QrezzyCustomizeQrDialog(
        qrBitmap = previewBitmap,
        style = uiState.qrStyleEditor.draftStyle,
        onQrColorSelected = viewModel::onQrColorSelected,
        onBackgroundColorSelected = viewModel::onBackgroundColorSelected,
        onPatternStyleSelected = viewModel::onPatternStyleSelected,
        onErrorCorrectionSelected = viewModel::onErrorCorrectionSelected,
        onCancelClick = viewModel::onDismissCustomizeQrDialog,
        onResetClick = viewModel::onResetQrStyleClick,
        onApplyClick = viewModel::onApplyQrStyleClick,
    )
}

@Composable
private fun GeneratorContent(
    uiState: GeneratorUiState,
    onQrInputSelected: (software.mazur.qrezzy.feature.generator.model.QrInput) -> Unit,
    onFormEvent: (software.mazur.qrezzy.feature.generator.model.QrInputField, String) -> Unit,
    onSaveClick: () -> Unit,
    onClearFocus: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .imePadding()
            .pointerInput(Unit) { detectTapGestures(onTap = { onClearFocus() }) },
        verticalArrangement = Arrangement.spacedBy(GeneratorScreenDefaults.sectionSpacing),
    ) {
        item {
            Spacer(modifier = Modifier.height(GeneratorScreenDefaults.sectionSpacing))
            QrTypeTabs(
                qrInputs = uiState.qrInputs,
                selectedQrInput = uiState.selectedQrInput,
                onQrInputSelected = onQrInputSelected,
            )
        }
        item {
            QrTypeForm(
                qrInput = uiState.selectedQrInput,
                fieldErrors = uiState.fieldErrors,
                onChange = onFormEvent,
            )
        }
        item {
            QrezzyButton(
                modifier = Modifier.padding(
                    top = GeneratorScreenDefaults.saveButtonTopPadding,
                    bottom = GeneratorScreenDefaults.saveButtonBottomPadding,
                ),
                elevation = GeneratorScreenDefaults.saveButtonElevation,
                text = stringResource(R.string.generator_save_qr_code),
                enabled = uiState.canSave,
                onClick = onSaveClick,
            )
        }
    }
}

private object GeneratorScreenDefaults {
    val screenHorizontalPadding = 16.dp
    val sectionSpacing = 16.dp
    val saveButtonTopPadding = 10.dp
    val saveButtonBottomPadding = 16.dp
    val saveButtonElevation = 0.dp
}