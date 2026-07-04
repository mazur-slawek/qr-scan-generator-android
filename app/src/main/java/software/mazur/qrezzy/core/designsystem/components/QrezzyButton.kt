package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.HighlightPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = QrezzyMint,
    depthColor: Color = QrezzyPurple,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    elevation: Dp = 4.dp,
) {
    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .height(QrezzyButtonDefaults.Container.height)
                .alpha(if (enabled) 1f else QrezzyButtonDefaults.DISABLED_ALPHA),
        contentAlignment = Alignment.Center,
    ) {
        val buttonWidth = maxWidth - QrezzyButtonDefaults.Depth.offsetX
        val buttonHeight = maxHeight - QrezzyButtonDefaults.Depth.offsetY

        Card(
            modifier =
                Modifier
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .align(Alignment.BottomEnd)
                    .border(
                        color = BorderPrimary,
                        shape = ShapeDefaults.Medium,
                        width = QrezzyButtonDefaults.Border.width,
                    ),
            shape = ShapeDefaults.Medium,
            colors = CardDefaults.cardColors(containerColor = depthColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        ) {}
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = ShapeDefaults.Medium,
            border =
                BorderStroke(
                    width = QrezzyButtonDefaults.Border.width,
                    color = BorderPrimary,
                ),
            contentPadding = PaddingValues.Zero,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = TextPrimary,
                    disabledContainerColor = containerColor,
                    disabledContentColor = TextPrimary,
                ),
            modifier =
                Modifier
                    .height(buttonHeight)
                    .width(buttonWidth)
                    .align(Alignment.TopStart),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .border(
                            width = QrezzyButtonDefaults.Border.highlightWidth,
                            color = HighlightPrimary.copy(alpha = QrezzyButtonDefaults.Border.HIGHLIGHT_ALPHA),
                            shape = ShapeDefaults.Medium,
                        ),
            ) {
                Text(
                    text = text,
                    maxLines = QrezzyButtonDefaults.MAX_LINES,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal =
                                    QrezzyButtonDefaults.Icon.width +
                                            QrezzyButtonDefaults.Content.horizontalPadding,
                            )
                            .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    autoSize =
                        TextAutoSize.StepBased(
                            minFontSize = QrezzyButtonDefaults.Text.minSize,
                            maxFontSize = QrezzyButtonDefaults.Text.maxSize,
                            stepSize = QrezzyButtonDefaults.Text.stepSize,
                        ),
                )

                leftIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(start = QrezzyButtonDefaults.Icon.horizontalPadding)
                                .height(buttonHeight)
                                .width(QrezzyButtonDefaults.Icon.width)
                                .align(Alignment.TopStart),
                    )
                }

                rightIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(end = QrezzyButtonDefaults.Icon.horizontalPadding)
                                .height(buttonHeight)
                                .width(QrezzyButtonDefaults.Icon.width)
                                .align(Alignment.TopEnd),
                    )
                }
            }
        }
    }
}

private object QrezzyButtonDefaults {
    object Container {
        val height = 60.dp
    }

    object Depth {
        val offsetX = 5.dp
        val offsetY = 5.5.dp
    }

    object Border {
        val width = 2.5.dp
        val highlightWidth = 4.dp
        const val HIGHLIGHT_ALPHA = 0.5f
    }

    object Icon {
        val width = 42.dp
        val horizontalPadding = 5.dp
    }

    object Content {
        val horizontalPadding = 15.dp
    }

    object Text {
        val minSize = 12.sp
        val maxSize = 18.sp
        val stepSize = 1.sp
    }

    const val MAX_LINES = 1
    const val DISABLED_ALPHA = 0.5f
}
