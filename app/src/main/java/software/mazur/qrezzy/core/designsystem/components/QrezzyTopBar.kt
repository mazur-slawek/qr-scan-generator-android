package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Immutable
data class QrezzyTopBarAction(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val iconTint: Color = TextPrimary,
    val enabled: Boolean = true,
)

@Composable
fun QrezzyTopBar(
    title: String,
    modifier: Modifier = Modifier,
    leftAction: QrezzyTopBarAction? = null,
    rightAction: QrezzyTopBarAction? = null,
) {
    Box(
        modifier = modifier
            .height(QrezzyTopBarDefaults.height)
            .fillMaxWidth(),
    ) {
        Text(
            text = title,
            maxLines = QrezzyTopBarDefaults.TITLE_MAX_LINES,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = QrezzyTopBarDefaults.titleHorizontalPadding),
        )

        leftAction?.let { action ->
            QrezzyTopBarActionButton(action = action, modifier = Modifier.align(Alignment.CenterStart))
        }

        rightAction?.let { action ->
            QrezzyTopBarActionButton(action = action, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
private fun QrezzyTopBarActionButton(action: QrezzyTopBarAction, modifier: Modifier = Modifier) {
    QrezzySmallButton(
        onClick = action.onClick,
        icon = action.icon,
        enabled = action.enabled,
        iconTint = action.iconTint,
        modifier = modifier,
    )
}

private object QrezzyTopBarDefaults {
    val height = 60.dp
    val titleHorizontalPadding = 74.dp
    const val TITLE_MAX_LINES = 2
}