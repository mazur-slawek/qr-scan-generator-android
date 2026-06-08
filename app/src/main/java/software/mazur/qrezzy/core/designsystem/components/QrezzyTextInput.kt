package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.mazur.qrezzy.core.designsystem.theme.BorderMedium
import software.mazur.qrezzy.core.designsystem.theme.Error
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextDisabled
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else QrezzyTextInputDefaults.TextArea.MAX_LINES,
    maxLength: Int? = null,
    isError: Boolean = false,
    errorText: String? = null,
    containerHeight: Dp = if (singleLine) {
        QrezzyTextInputDefaults.Container.singleLineHeight
    } else {
        QrezzyTextInputDefaults.Container.textAreaHeight
    },
    containerColor: Color = Surface,
    depthColor: Color = QrezzyMint,
    errorColor: Color = Error,
    focusedBorderColor: Color = QrezzyPurpleDark,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = TextPrimary,
        fontSize = QrezzyTextInputDefaults.Text.fontSize,
        fontWeight = FontWeight.Medium,
        lineHeight = QrezzyTextInputDefaults.Text.lineHeight,
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val targetBorderColor = when {
        isError   -> errorColor
        isFocused -> focusedBorderColor
        else      -> BorderMedium
    }
    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        label = "QrezzyTextInputBorderColor",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused || isError) {
            QrezzyTextInputDefaults.Border.focusedWidth
        } else {
            QrezzyTextInputDefaults.Border.width
        },
        label = "QrezzyTextInputBorderWidth",
    )
    val contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    val contentPadding = if (singleLine) {
        QrezzyTextInputDefaults.Content.singleLinePadding
    } else {
        QrezzyTextInputDefaults.Content.textAreaPadding
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BoxWithConstraints(
            modifier = Modifier
                .height(containerHeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val inputWidth = maxWidth - QrezzyTextInputDefaults.Depth.offsetX
            val inputHeight = maxHeight - QrezzyTextInputDefaults.Depth.offsetY

            Spacer(
                modifier = Modifier
                    .height(inputHeight)
                    .width(inputWidth)
                    .background(color = depthColor, shape = ShapeDefaults.Medium)
                    .border(
                        width = QrezzyTextInputDefaults.Border.width,
                        color = BorderMedium,
                        shape = ShapeDefaults.Medium,
                    )
                    .align(Alignment.BottomEnd),
            )

            Box(
                modifier = Modifier
                    .height(inputHeight)
                    .width(inputWidth)
                    .align(Alignment.TopStart)
                    .background(color = containerColor, shape = ShapeDefaults.Medium)
                    .border(
                        width = borderWidth,
                        color = borderColor,
                        shape = ShapeDefaults.Medium,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    contentAlignment = contentAlignment,
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = { newValue ->
                            val limitedValue = maxLength?.let(newValue::take) ?: newValue
                            onValueChange(limitedValue)
                        },
                        enabled = enabled,
                        singleLine = singleLine,
                        minLines = minLines,
                        maxLines = maxLines,
                        textStyle = textStyle,
                        visualTransformation = visualTransformation,
                        interactionSource = interactionSource,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        modifier = Modifier.fillMaxSize(),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = contentAlignment,
                            ) {
                                if (value.isEmpty() && placeholder.isNotEmpty()) {
                                    Text(text = placeholder, color = TextDisabled, style = textStyle)
                                }
                                innerTextField()
                            }
                        },
                    )
                }
            }
        }

        if (!errorText.isNullOrBlank()) {
            Text(
                text = errorText,
                color = errorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = QrezzyTextInputDefaults.ErrorText.startPadding,
                    top = QrezzyTextInputDefaults.ErrorText.topPadding,
                ),
            )
        }
    }
}

private object QrezzyTextInputDefaults {
    object Container {
        val singleLineHeight = 60.dp
        val textAreaHeight = 140.dp
    }

    object Depth {
        val offsetX = 4.dp
        val offsetY = 3.5.dp
    }

    object Border {
        val width = 1.5.dp
        val focusedWidth = 2.dp
    }

    object Content {
        val singleLinePadding = PaddingValues(horizontal = 16.dp)
        val textAreaPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 14.dp,
        )
    }

    object Text {
        val fontSize = 18.sp
        val lineHeight = 22.sp
    }

    object TextArea {
        const val MAX_LINES = 5
    }

    object ErrorText {
        val startPadding = 8.dp
        val topPadding = 4.dp
    }
}