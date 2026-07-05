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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark

@Composable
fun QrezzyListSection(
    modifier: Modifier = Modifier,
    isFocused: Boolean = false,
    title: String? = null,
    error: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isError = !error.isNullOrBlank()
    val backgroundColor = when {
        isError -> QrezzyPink
        isFocused -> QrezzyPurple
        else -> QrezzyMint
    }
    val borderColor = when {
        isError -> QrezzyPinkDark
        isFocused -> QrezzyPurpleDark
        else -> MaterialTheme.colorScheme.surfaceDim
    }
    Column(modifier = modifier.fillMaxWidth()) {
        title?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(
                    start = QrezzyListSectionDefaults.Title.startPadding,
                    bottom = QrezzyListSectionDefaults.Title.bottomPadding
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = QrezzyListSectionDefaults.Depth.offsetY,
                    start = QrezzyListSectionDefaults.Depth.offsetX
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = backgroundColor,
                        shape = QrezzyListSectionDefaults.shape
                    )
                    .border(
                        color = borderColor,
                        shape = QrezzyListSectionDefaults.shape,
                        width = QrezzyListSectionDefaults.borderWidth
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset(
                        x = -QrezzyListSectionDefaults.Depth.offsetX,
                        y = -QrezzyListSectionDefaults.Depth.offsetY
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = QrezzyListSectionDefaults.shape
                    )
                    .border(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = QrezzyListSectionDefaults.shape,
                        width = QrezzyListSectionDefaults.borderWidth
                    )
                    .clip(QrezzyListSectionDefaults.shape),
                content = content
            )
        }
        if (isError) {
            Text(
                text = error,
                color = QrezzyPinkDark,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = QrezzyListSectionDefaults.Error.startPadding,
                    top = QrezzyListSectionDefaults.Error.topPadding
                )
            )
        }
    }
}

private object QrezzyListSectionDefaults {
    val shape = RoundedCornerShape(10.dp)
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
