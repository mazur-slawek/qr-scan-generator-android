package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QrezzyTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else QrezzyTextInputDefaults.TextArea.MAX_LINES,
    maxLength: Int? = null,
    errorText: String? = null,
    containerHeight: Dp = if (singleLine) {
        QrezzyTextInputDefaults.Container.singleLineHeight
    } else {
        QrezzyTextInputDefaults.Container.textAreaHeight
    },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = QrezzyTextInputDefaults.Text.fontSize,
        fontWeight = FontWeight.Medium,
        lineHeight = QrezzyTextInputDefaults.Text.lineHeight
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    val contentPadding = if (singleLine) {
        QrezzyTextInputDefaults.Content.singleLinePadding
    } else {
        QrezzyTextInputDefaults.Content.textAreaPadding
    }
    Column(modifier = modifier.fillMaxWidth()) {
        QrezzyListSection(title = title, isFocused = isFocused, error = errorText) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    val limitedValue = maxLength?.let { limit -> newValue.take(limit + 1) } ?: newValue
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
                        modifier = Modifier
                            .fillMaxSize()
                            .height(containerHeight)
                            .padding(contentPadding),
                        contentAlignment = contentAlignment
                    ) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = textStyle
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

private object QrezzyTextInputDefaults {
    object Container {
        val singleLineHeight = 60.dp
        val textAreaHeight = 140.dp
    }

    object Content {
        val singleLinePadding = PaddingValues(horizontal = 16.dp)
        val textAreaPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    }

    object Text {
        val fontSize = 18.sp
        val lineHeight = 22.sp
    }

    object TextArea {
        const val MAX_LINES = 5
    }
}
