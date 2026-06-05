package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyButton(
    text: String,
    modifier: Modifier = Modifier,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .height(QrezzyButtonDefaults.ContainerHeight)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val buttonWidth = maxWidth - QrezzyButtonDefaults.ShadowOffsetX
        val buttonHeight = maxHeight - QrezzyButtonDefaults.ShadowOffsetY

        Spacer(
            modifier = Modifier
                .height(buttonHeight)
                .width(buttonWidth)
                .background(
                    color = QrezzyPurple,
                    shape = ShapeDefaults.Medium,
                )
                .border(
                    width = QrezzyButtonDefaults.BorderWidth,
                    color = Color.Black,
                    shape = ShapeDefaults.Medium,
                )
                .align(Alignment.BottomEnd),
        )

        Button(
            onClick = onClick,
            enabled = enabled,
            shape = ShapeDefaults.Medium,
            border = BorderStroke(
                width = QrezzyButtonDefaults.BorderWidth,
                color = Color.Black,
            ),
            contentPadding = PaddingValues.Zero,
            colors = ButtonDefaults.buttonColors(
                containerColor = QrezzyMint,
                contentColor = TextPrimary,
                disabledContainerColor = QrezzyMint.copy(
                    alpha = QrezzyButtonDefaults.DisabledAlpha,
                ),
                disabledContentColor = TextPrimary.copy(
                    alpha = QrezzyButtonDefaults.DisabledAlpha,
                ),
            ),
            modifier = Modifier
                .height(buttonHeight)
                .width(buttonWidth)
                .align(Alignment.TopStart)
                .shadow(
                    elevation = QrezzyButtonDefaults.ShadowElevation,
                    shape = ShapeDefaults.Medium,
                    ambientColor = Color.Black,
                    spotColor = Color.Black,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = QrezzyButtonDefaults.InnerHighlightBorderWidth,
                        color = Color.White.copy(
                            alpha = QrezzyButtonDefaults.InnerHighlightAlpha,
                        ),
                        shape = ShapeDefaults.Medium,
                    ),
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QrezzyButtonDefaults.IconWidth + 15.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = QrezzyButtonDefaults.MinTextSize,
                        maxFontSize = QrezzyButtonDefaults.MaxTextSize,
                        stepSize = QrezzyButtonDefaults.TextSizeStep
                    ),
                )
                leftIcon?.let {icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .height(buttonHeight)
                            .width(QrezzyButtonDefaults.IconWidth)
                            .align(Alignment.TopStart),
                    )
                }
                rightIcon?.let {icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .height(buttonHeight)
                            .width(QrezzyButtonDefaults.IconWidth)
                            .align(Alignment.TopEnd),
                    )
                }
            }
        }
    }
}

private object QrezzyButtonDefaults {
    val ContainerHeight = 62.dp
    val ShadowOffsetX = 5.dp
    val ShadowOffsetY = 5.5.dp
    val ShadowElevation = 8.dp
    val BorderWidth = 2.5.dp
    val InnerHighlightBorderWidth = 4.dp
    val IconWidth = 42.dp
    val MinTextSize = 12.sp
    val MaxTextSize = 18.sp
    val TextSizeStep = 1.sp
    const val DisabledAlpha = 0.5f
    const val InnerHighlightAlpha = 0.5f
}