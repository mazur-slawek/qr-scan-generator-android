package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    title: String,
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = QrezzyButtonDefaults.InnerHighlightBorderWidth,
                        color = Color.White.copy(
                            alpha = QrezzyButtonDefaults.InnerHighlightAlpha,
                        ),
                        shape = ShapeDefaults.Medium,
                    )
                    .padding(
                        horizontal = QrezzyButtonDefaults.HorizontalContentPadding,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leftIcon?.let {icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(QrezzyButtonDefaults.LeftIconSize),
                    )
                }

                QrezzyButtonText(
                    text = title,
                    modifier = Modifier.weight(QrezzyButtonDefaults.TitleWeight),
                )

                rightIcon?.let {icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(QrezzyButtonDefaults.RightIconSize),
                    )
                }
            }
        }
    }
}

@Composable
private fun QrezzyButtonText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        autoSize = TextAutoSize.StepBased(
            minFontSize = QrezzyButtonDefaults.MinTextSize,
            maxFontSize = QrezzyButtonDefaults.MaxTextSize,
            stepSize = QrezzyButtonDefaults.TextSizeStep
        ),
    )
}

private object QrezzyButtonDefaults {
    val ContainerHeight = 62.dp
    val ShadowOffsetX = 5.dp
    val ShadowOffsetY = 5.5.dp
    val ShadowElevation = 8.dp
    val BorderWidth = 2.5.dp
    val InnerHighlightBorderWidth = 4.dp
    val HorizontalContentPadding = 12.dp
    val LeftIconSize = 32.dp
    val RightIconSize = 42.dp
    val MinTextSize = 12.sp
    val MaxTextSize = 18.sp
    val TextSizeStep = 1.sp
    const val DisabledAlpha = 0.5f
    const val InnerHighlightAlpha = 0.5f
    const val TitleWeight = 1f
}