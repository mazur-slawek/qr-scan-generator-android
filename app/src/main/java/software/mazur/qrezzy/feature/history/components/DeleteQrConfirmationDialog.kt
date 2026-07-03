package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

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
        DeleteQrConfirmationDialogContent(
            title = title,
            subtitle = subtitle,
            onCancelClick = onCancelClick,
            onConfirmClick = onConfirmClick,
        )
    }
}

@Composable
private fun DeleteQrConfirmationDialogContent(
    title: String,
    subtitle: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DeleteQrConfirmationDialogDefaults.Container.outerPadding)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = DeleteQrConfirmationDialogDefaults.Container.shape,
            )
            .border(
                color = BorderPrimary,
                shape = DeleteQrConfirmationDialogDefaults.Container.shape,
                width = DeleteQrConfirmationDialogDefaults.Container.borderWidth,
            )
            .clip(DeleteQrConfirmationDialogDefaults.Container.shape)
            .padding(DeleteQrConfirmationDialogDefaults.Container.contentPadding),
    ) {
        DeleteQrConfirmationMessageCard(
            title = title,
            subtitle = subtitle,
        )

        Spacer(modifier = Modifier.height(DeleteQrConfirmationDialogDefaults.Actions.topSpacing))

        DeleteQrConfirmationActions(
            onCancelClick = onCancelClick,
            onConfirmClick = onConfirmClick,
        )
    }
}

@Composable
private fun DeleteQrConfirmationMessageCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = DeleteQrConfirmationDialogDefaults.MessageCard.borderWidth,
                color = BorderLight,
                shape = DeleteQrConfirmationDialogDefaults.MessageCard.shape,
            )
            .background(
                color = Surface,
                shape = DeleteQrConfirmationDialogDefaults.MessageCard.shape,
            )
            .padding(
                horizontal = DeleteQrConfirmationDialogDefaults.MessageCard.horizontalPadding,
                vertical = DeleteQrConfirmationDialogDefaults.MessageCard.verticalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.qrezzy_mascot_delete),
            contentDescription = null,
            modifier = Modifier.size(DeleteQrConfirmationDialogDefaults.Illustration.size),
        )

        Spacer(modifier = Modifier.height(DeleteQrConfirmationDialogDefaults.MessageCard.imageTextSpacing))

        Text(
            text = title,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(DeleteQrConfirmationDialogDefaults.MessageCard.textSpacing))

        Text(
            text = subtitle,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun DeleteQrConfirmationActions(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        QrezzyButton(
            text = stringResource(R.string.common_delete),
            containerColor = QrezzyPink,
            depthColor = QrezzyYellowDark,
            onClick = onConfirmClick,
            elevation = DeleteQrConfirmationDialogDefaults.Actions.buttonElevation,
        )

        Spacer(modifier = Modifier.height(DeleteQrConfirmationDialogDefaults.Actions.buttonSpacing))

        QrezzyButton(
            text = stringResource(R.string.common_cancel),
            containerColor = Surface,
            onClick = onCancelClick,
            elevation = DeleteQrConfirmationDialogDefaults.Actions.buttonElevation,
        )
    }
}

private object DeleteQrConfirmationDialogDefaults {
    const val DEFAULT_COUNT = 1

    object Container {
        val shape = ShapeDefaults.Large
        val outerPadding = 16.dp
        val contentPadding = 16.dp
        val borderWidth = 2.dp
    }

    object MessageCard {
        val shape = ShapeDefaults.Medium
        val borderWidth = 0.5.dp
        val horizontalPadding = 16.dp
        val verticalPadding = 16.dp
        val imageTextSpacing = 16.dp
        val textSpacing = 8.dp
    }

    object Illustration {
        val size = 170.dp
    }

    object Actions {
        val topSpacing = 16.dp
        val buttonSpacing = 8.dp
        val buttonElevation = 0.dp
    }
}