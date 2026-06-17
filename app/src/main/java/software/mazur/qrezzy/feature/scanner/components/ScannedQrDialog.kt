package software.mazur.qrezzy.feature.scanner.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.domain.history.model.QrHistoryType

@Composable
fun ScannedQrDialog(
    type: QrHistoryType?,
    title: String,
    content: String,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = {
            Column() {
                Text(text = "QR code detected!")
                Text(text = "QR detected")
            }
        },
        text = {
            Column {
                Text(text = "Type: ${type?.name.orEmpty()}")

                Spacer(modifier = Modifier.height(8.dp))

                if (title.isNotBlank()) {
                    Text(text = title)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(text = content)
            }
        },
        confirmButton = {
            QrezzyButton(text = "Save", onClick = onSaveClick)
        },
        dismissButton = {
            QrezzyButton(text = "Cancel", onClick = onCancelClick)
        },
    )
}