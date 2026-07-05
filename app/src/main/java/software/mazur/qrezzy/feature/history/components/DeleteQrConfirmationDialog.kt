package software.mazur.qrezzy.feature.history.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.window.DialogProperties
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.qrezzyQr.QrezzyDeleteConfirmation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteQrConfirmationDialog(
    count: Int = DeleteQrConfirmationDialogDefaults.DEFAULT_COUNT,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val title = pluralStringResource(
        id = R.plurals.delete_confirmation_dialog_title,
        count = count,
        count,
    )
    val subtitle = pluralStringResource(
        id = R.plurals.delete_confirmation_dialog_subtitle,
        count = count,
        count,
    )

    AlertDialog(
        onDismissRequest = onCancelClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        QrezzyDeleteConfirmation(
            title = title,
            subtitle = subtitle,
            onCancelClick = onCancelClick,
            onConfirmClick = onConfirmClick,
        )
    }
}


private object DeleteQrConfirmationDialogDefaults {
    const val DEFAULT_COUNT = 1
}