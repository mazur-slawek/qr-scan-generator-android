package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun QrezzyCircleButton(
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(QrezzyCircleButtonDefaults.Container.size),
        shape = CircleShape,
        border = BorderStroke(width = QrezzyCircleButtonDefaults.Border.width,
            color = MaterialTheme.colorScheme.surfaceDim),
        contentPadding = PaddingValues.Zero,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = iconTint,
                disabledContainerColor = color.copy(alpha = QrezzyCircleButtonDefaults.DISABLED_ALPHA),
                disabledContentColor = iconTint.copy(alpha = QrezzyCircleButtonDefaults.DISABLED_ALPHA),
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(QrezzyCircleButtonDefaults.Icon.size),
            tint = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

private object QrezzyCircleButtonDefaults {
    object Container {
        val size = 60.dp
    }

    object Border {
        val width = 2.5.dp
    }

    object Icon {
        val size = 32.dp
    }

    const val DISABLED_ALPHA = 0.5f
}
