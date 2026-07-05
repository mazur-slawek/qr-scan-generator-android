package software.mazur.qrezzy.feature.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyDeleteConfirmation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearHistoryConfirmationDialog(onConfirmClick: () -> Unit, onCancelClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        QrezzyDeleteConfirmation(
            title = stringResource(R.string.clear_history_confirmation_title),
            subtitle = stringResource(R.string.clear_history_confirmation_subtitle),
            onCancelClick = onCancelClick,
            onConfirmClick = onConfirmClick
        )
    }
}
