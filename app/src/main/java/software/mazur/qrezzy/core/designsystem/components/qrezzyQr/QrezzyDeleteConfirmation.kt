package software.mazur.qrezzy.core.designsystem.components.qrezzyQr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark

@Composable
fun QrezzyDeleteConfirmation(title: String, subtitle: String, onCancelClick: () -> Unit, onConfirmClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(QrezzyDeleteConfirmationDefault.Container.outerPadding)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = QrezzyDeleteConfirmationDefault.Container.shape,
            )
            .border(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = QrezzyDeleteConfirmationDefault.Container.shape,
                width = QrezzyDeleteConfirmationDefault.Container.borderWidth,
            )
            .clip(QrezzyDeleteConfirmationDefault.Container.shape)
            .padding(QrezzyDeleteConfirmationDefault.Container.contentPadding),
    ) {
        DeleteQrConfirmationMessageCard(
            title = title,
            subtitle = subtitle,
        )

        Spacer(modifier = Modifier.height(QrezzyDeleteConfirmationDefault.Actions.topSpacing))

        DeleteQrConfirmationActions(
            onCancelClick = onCancelClick,
            onConfirmClick = onConfirmClick,
        )
    }
}

@Composable
private fun DeleteQrConfirmationMessageCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = QrezzyDeleteConfirmationDefault.MessageCard.borderWidth,
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = QrezzyDeleteConfirmationDefault.MessageCard.shape,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = QrezzyDeleteConfirmationDefault.MessageCard.shape,
            )
            .padding(
                horizontal = QrezzyDeleteConfirmationDefault.MessageCard.horizontalPadding,
                vertical = QrezzyDeleteConfirmationDefault.MessageCard.verticalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.qrezzy_mascot_delete),
            contentDescription = null,
            modifier = Modifier.size(QrezzyDeleteConfirmationDefault.Illustration.size),
        )

        Spacer(modifier = Modifier.height(QrezzyDeleteConfirmationDefault.MessageCard.imageTextSpacing))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(QrezzyDeleteConfirmationDefault.MessageCard.textSpacing))

        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            elevation = QrezzyDeleteConfirmationDefault.Actions.buttonElevation,
        )

        Spacer(modifier = Modifier.height(QrezzyDeleteConfirmationDefault.Actions.buttonSpacing))

        QrezzyButton(
            text = stringResource(R.string.common_cancel),
            containerColor = QrezzyYellow,
            depthColor = QrezzyPinkDark,
            onClick = onCancelClick,
            elevation = QrezzyDeleteConfirmationDefault.Actions.buttonElevation,
        )
    }
}

private object QrezzyDeleteConfirmationDefault {
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