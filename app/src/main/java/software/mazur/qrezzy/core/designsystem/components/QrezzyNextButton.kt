package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyNextButton(
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(QrezzyNextButtonDefaults.BUTTON_SIZE),
        shape = ShapeDefaults.Large.copy(
            all = CornerSize(QrezzyNextButtonDefaults.CORNER_RADIUS),
        ),
        border = BorderStroke(
            width = QrezzyNextButtonDefaults.BORDER_WIDTH,
            color = Color.Black,
        ),
        contentPadding = PaddingValues.Zero,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = TextPrimary,
            disabledContainerColor = color.copy(
                alpha = QrezzyNextButtonDefaults.DISABLED_ALPHA,
            ),
            disabledContentColor = TextPrimary.copy(
                alpha = QrezzyNextButtonDefaults.DISABLED_ALPHA,
            ),
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(QrezzyNextButtonDefaults.ICON_SIZE)
                .padding(QrezzyNextButtonDefaults.ICON_PADDING),
        )
    }
}

private object QrezzyNextButtonDefaults {
    val BUTTON_SIZE = 60.dp
    val ICON_SIZE = 42.dp
    val ICON_PADDING = 5.dp
    val CORNER_RADIUS = 100.dp
    val BORDER_WIDTH = 2.5.dp
    const val DISABLED_ALPHA = 0.5f
}