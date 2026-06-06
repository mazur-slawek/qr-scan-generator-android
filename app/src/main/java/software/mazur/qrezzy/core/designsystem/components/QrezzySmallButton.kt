package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzySmallButton(
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    BoxWithConstraints(
        modifier = modifier.size(QrezzySmallButtonDefaults.BUTTON_SIZE),
        contentAlignment = Alignment.Center,
    ) {
        val foregroundWidth = maxWidth - QrezzySmallButtonDefaults.SHADOW_OFFSET
        val foregroundHeight = maxHeight - QrezzySmallButtonDefaults.SHADOW_OFFSET
        val foregroundModifier = Modifier
            .width(foregroundWidth)
            .height(foregroundHeight)

        Spacer(
            modifier = foregroundModifier
                .background(
                    color = QrezzySmallButtonDefaults.SHADOW_COLOR,
                    shape = ShapeDefaults.Medium,
                )
                .border(
                    width = QrezzySmallButtonDefaults.SHADOW_BORDER_WIDTH,
                    color = QrezzySmallButtonDefaults.BORDER_COLOR,
                    shape = ShapeDefaults.Medium,
                )
                .align(Alignment.BottomEnd),
        )

        Button(
            onClick = onClick,
            enabled = enabled,
            shape = ShapeDefaults.Medium,
            border = BorderStroke(
                width = QrezzySmallButtonDefaults.BORDER_WIDTH,
                color = QrezzySmallButtonDefaults.BORDER_COLOR,
            ),
            contentPadding = PaddingValues.Zero,
            colors = ButtonDefaults.buttonColors(
                containerColor = QrezzySmallButtonDefaults.CONTAINER_COLOR,
                contentColor = TextPrimary,
                disabledContainerColor = QrezzySmallButtonDefaults.CONTAINER_COLOR,
            ),
            modifier = foregroundModifier.align(Alignment.TopStart),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = QrezzySmallButtonDefaults.INNER_HIGHLIGHT_WIDTH,
                        color = QrezzySmallButtonDefaults.INNER_HIGHLIGHT_COLOR,
                        shape = ShapeDefaults.Medium,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(QrezzySmallButtonDefaults.ICON_SIZE),
                )
            }
        }
    }
}

private object QrezzySmallButtonDefaults {
    val BUTTON_SIZE = 45.dp
    val SHADOW_OFFSET = 2.5.dp
    val BORDER_WIDTH = 1.5.dp
    val SHADOW_BORDER_WIDTH = 1.dp
    val INNER_HIGHLIGHT_WIDTH = 2.dp
    val ICON_SIZE = 24.dp
    val CONTAINER_COLOR = Color.White
    val SHADOW_COLOR = QrezzyMint
    val BORDER_COLOR = Color.Black
    val INNER_HIGHLIGHT_COLOR = Color.White.copy(alpha = INNER_HIGHLIGHT_ALPHA)
    const val DISABLED_ALPHA = 0.5f
    private const val INNER_HIGHLIGHT_ALPHA = 0.5f
}