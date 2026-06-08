package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
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
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzySmallButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = TextPrimary,
    containerColor: Color = Surface,
    depthColor: Color = QrezzyMint,
) {
    BoxWithConstraints(
        modifier = modifier.size(QrezzySmallButtonDefaults.Container.size),
        contentAlignment = Alignment.Center,
    ) {
        val foregroundWidth = maxWidth - QrezzySmallButtonDefaults.Depth.offset
        val foregroundHeight = maxHeight - QrezzySmallButtonDefaults.Depth.offset
        val foregroundModifier = Modifier
            .width(foregroundWidth)
            .height(foregroundHeight)

        Box(
            modifier = foregroundModifier
                .background(color = depthColor, shape = ShapeDefaults.Medium)
                .border(
                    width = QrezzySmallButtonDefaults.Depth.borderWidth,
                    color = BorderPrimary,
                    shape = ShapeDefaults.Medium,
                )
                .align(Alignment.BottomEnd),
        )

        Button(
            onClick = onClick,
            enabled = enabled,
            shape = ShapeDefaults.Medium,
            border = BorderStroke(width = QrezzySmallButtonDefaults.Border.width, color = BorderPrimary),
            contentPadding = PaddingValues.Zero,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = TextPrimary,
                disabledContainerColor = containerColor,
                disabledContentColor = TextPrimary,
            ),
            modifier = foregroundModifier.align(Alignment.TopStart),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(QrezzySmallButtonDefaults.Icon.size),
                )
            }
        }
    }

}

private object QrezzySmallButtonDefaults {
    object Container {
        val size = 45.dp
    }

    object Depth {
        val offset = 2.5.dp
        val borderWidth = 1.dp
    }

    object Border {
        val width = 1.5.dp
    }

    object Icon {
        val size = 24.dp
    }
}