package software.mazur.qrezzy.feature.scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails.QrezzyQrInfo
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.domain.qr.model.Qr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannedQrDialog(qr: Qr, onSaveClick: () -> Unit, onCancelClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ScannedQrDialogDefaults.outerPadding)
                .background(
                    color = Surface,
                    shape = ScannedQrDialogDefaults.shape,
                )
                .border(
                    width = ScannedQrDialogDefaults.Border.width,
                    color = BorderPrimary,
                    shape = ScannedQrDialogDefaults.shape,
                )
                .clip(ScannedQrDialogDefaults.shape)
                .padding(ScannedQrDialogDefaults.contentPadding),
        ) {
            QrezzyTopBar(
                title = stringResource(R.string.scanner_dialog_title),
                subtitle = stringResource(R.string.scanner_dialog_subtitle)
            ) {
                QrezzyTopBarButton(onClick = onCancelClick, icon = Icons.Outlined.Close, iconTint = QrezzyPurpleDark)
            }
            Spacer(modifier = Modifier.height(ScannedQrDialogDefaults.contentSpacing))
            QrezzyQrInfo(qr = qr)
            Spacer(modifier = Modifier.height(ScannedQrDialogDefaults.contentSpacing))
            QrezzyButton(
                text = stringResource(R.string.scanner_dialog_save),
                onClick = onSaveClick,
                elevation = 0.dp,
            )
        }
    }
}

private object ScannedQrDialogDefaults {
    val shape = ShapeDefaults.Large
    val outerPadding = 16.dp
    val contentPadding = 16.dp
    val contentSpacing = 16.dp

    object Border {
        val width = 2.dp
    }
}