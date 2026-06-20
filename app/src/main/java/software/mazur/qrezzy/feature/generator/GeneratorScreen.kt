package software.mazur.qrezzy.feature.generator

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.feature.generator.components.QrPreview
import software.mazur.qrezzy.feature.generator.components.QrTypeForm
import software.mazur.qrezzy.feature.generator.components.QrTypeTabs
import software.mazur.qrezzy.feature.generator.model.GeneratorUiEvent

@Composable
fun GeneratorScreen(viewModel: GeneratorViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.value
    val qrBitmap =
        remember(uiState.qrContent) {
            viewModel.generateQrBitmap(uiState.qrContent)
        }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val qrSavedMessage = stringResource(R.string.generator_qr_saved_message)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GeneratorUiEvent.QrSaved -> {
                    Toast.makeText(context, qrSavedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_generate,
            subtitleResId = R.string.navigation_subtitle_generate
        ) {
            QrezzyTopBarButton(
                onClick = {},
                enabled = uiState.canSave,
                icon = Icons.Outlined.FormatPaint,
                iconTint = if (uiState.canSave) QrezzyPurpleDark else Color.Gray,
            )
        }

        QrPreview(
            qrBitmap = qrBitmap,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 10.dp)
                    .height(200.dp),
        )

        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .imePadding()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { focusManager.clearFocus() },
                        )
                    },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(R.string.generator_select_qr_type),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                QrTypeTabs(
                    qrInputs = uiState.qrInputs,
                    selectedQrInput = uiState.selectedQrInput,
                    onQrInputSelected = viewModel::onQrInputSelected,
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(R.string.generator_enter_data),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                QrTypeForm(
                    qrInput = uiState.selectedQrInput,
                    fieldErrors = uiState.fieldErrors,
                    onChange = viewModel::onFormEvent,
                )
            }

            item {
                QrezzyButton(
                    modifier = Modifier.padding(bottom = 16.dp, top = 10.dp),
                    elevation = 0.dp,
                    text = stringResource(R.string.generator_save_qr_code),
                    enabled = uiState.canSave,
                    onClick = viewModel::saveQrCode,
                )
            }
        }
    }
}
