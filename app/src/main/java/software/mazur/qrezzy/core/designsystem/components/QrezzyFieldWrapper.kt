package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.Error
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyFieldWrapper(
    modifier: Modifier = Modifier,
    isFocused: Boolean = false,
    title: String? = null,
    error: String? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val isError = !error.isNullOrBlank()
    val borderColor = if (isError) QrezzyPink else if (isFocused) QrezzyPurple else QrezzyMint
    val backgroundColor = if (isError) QrezzyPinkDark else if (isFocused) QrezzyPurpleDark else BorderPrimary

    Column(modifier = modifier.fillMaxWidth()) {
        title?.let { value ->
            Text(
                text = value,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    start = QrezzyFieldWrapperDefaults.Title.startPadding,
                    bottom = QrezzyFieldWrapperDefaults.Title.bottomPadding,
                ),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = QrezzyFieldWrapperDefaults.Depth.offsetX,
                    top = QrezzyFieldWrapperDefaults.Depth.offsetY,
                ),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(
                        x = -QrezzyFieldWrapperDefaults.Depth.offsetX,
                        y = -QrezzyFieldWrapperDefaults.Depth.offsetY
                    )
                    .background(color = borderColor, shape = QrezzyFieldWrapperDefaults.shape)
                    .border(
                        width = QrezzyFieldWrapperDefaults.borderWidth,
                        color = backgroundColor,
                        shape = QrezzyFieldWrapperDefaults.shape,
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Surface, shape = QrezzyFieldWrapperDefaults.shape)
                    .border(
                        width = QrezzyFieldWrapperDefaults.borderWidth,
                        color = BorderPrimary,
                        shape = QrezzyFieldWrapperDefaults.shape,
                    )
                    .clip(QrezzyFieldWrapperDefaults.shape),
                content = content,
            )
        }
        if (isError) {
            Text(
                text = error,
                color = Error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = QrezzyFieldWrapperDefaults.Error.startPadding,
                    top = QrezzyFieldWrapperDefaults.Error.topPadding,
                )
            )
        }
    }
}

private object QrezzyFieldWrapperDefaults {
    val shape = ShapeDefaults.Medium
    val borderWidth = 1.5.dp

    object Title {
        val startPadding = 4.dp
        val bottomPadding = 6.dp
    }

    object Depth {
        val offsetX = 3.5.dp
        val offsetY = 4.dp
    }

    object Error {
        val startPadding = 8.dp
        val topPadding = 4.dp
    }
}