package software.mazur.qrezzy.feature.generator.presentation

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarAction
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.feature.generator.presentation.components.QrPreview
import software.mazur.qrezzy.feature.generator.presentation.components.QrTypeForm
import software.mazur.qrezzy.feature.generator.presentation.components.QrTypeTabs

@Composable
fun GeneratorScreen(viewModel: GeneratorViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.value
    val qrBitmap = remember(uiState.qrContent) { viewModel.generateQrBitmap(uiState.qrContent) }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        QrezzyTopBar(
            title = stringResource(R.string.navigation_title_generate),
            rightAction = QrezzyTopBarAction(
                enabled = uiState.canSave,
                icon = Icons.Outlined.FormatPaint,
                iconTint = if (uiState.canSave) QrezzyPurpleDark else Color.Gray,
                onClick = {}
            ),
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                QrPreview(qrBitmap = qrBitmap, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 10.dp)
                    .height(200.dp))
            }
            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Select QR code type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                QrTypeTabs(selectedType = uiState.selectedType, onTypeSelected = viewModel::onTypeSelected)
            }
            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Enter data to generate a QR code",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                QrTypeForm(
                    selectedType = uiState.selectedType,
                    fieldErrors = uiState.fieldErrors,
                    onChange = viewModel::onFormEvent
                )
            }
            item {
                QrezzyButton(
                    modifier = Modifier.padding(bottom = 16.dp, top = 10.dp),
                    elevation = 0.dp,
                    text = "Save QR Code",
                    enabled = uiState.canSave,
                    onClick = {},
                )
            }
        }
    }
}