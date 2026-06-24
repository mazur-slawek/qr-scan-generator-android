package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.BorderSecondary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyFieldWrapper(
    modifier: Modifier = Modifier,
    height: Dp = QrezzyFieldWrapperDefaults.height,
    focused: Boolean = false,
    title: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Column {
        title?.let {
            Text(
                text = title,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    start = QrezzyFieldWrapperDefaults.titleStartPadding,
                    bottom = QrezzyFieldWrapperDefaults.titleBottomPadding,
                )
            )
        }
        BoxWithConstraints(
            modifier = modifier
                .height(height)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val fieldWidth = maxWidth - QrezzyFieldWrapperDefaults.Depth.offsetX
            val fieldHeight = maxHeight - QrezzyFieldWrapperDefaults.Depth.offsetY
            val borderColor = if (focused) QrezzyMintDark else BorderSecondary
            val borderWidth =
                if (focused) QrezzyFieldWrapperDefaults.focusedBorderWidth else QrezzyFieldWrapperDefaults.borderWidth

            Spacer(
                modifier = Modifier
                    .height(fieldHeight)
                    .width(fieldWidth)
                    .background(color = QrezzyMint, shape = QrezzyFieldWrapperDefaults.shape)
                    .border(
                        width = QrezzyFieldWrapperDefaults.borderWidth,
                        color = BorderSecondary,
                        shape = QrezzyFieldWrapperDefaults.shape,
                    )
                    .align(Alignment.TopStart),
            )

            Box(
                modifier = Modifier
                    .height(fieldHeight)
                    .width(fieldWidth)
                    .background(color = Surface, shape = QrezzyFieldWrapperDefaults.shape)
                    .border(width = borderWidth, color = borderColor, shape = QrezzyFieldWrapperDefaults.shape)
                    .align(Alignment.BottomEnd),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart,
                    content = content,
                )
            }
        }
    }
}

@Composable
fun QrezzyRowFieldWrapper(
    modifier: Modifier = Modifier,
    height: Dp = QrezzyFieldWrapperDefaults.height,
    focused: Boolean = false,
    title: String? = null,
    scrollable: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    QrezzyFieldWrapper(title = title, modifier = modifier, height = height, focused = focused) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
                .padding(
                    vertical = QrezzyFieldWrapperDefaults.contentVerticalPadding,
                    horizontal = QrezzyFieldWrapperDefaults.contentHorizontalPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(QrezzyFieldWrapperDefaults.itemSpacing),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }

}

private object QrezzyFieldWrapperDefaults {
    val height = 75.dp
    val shape = ShapeDefaults.Medium
    val borderWidth = 1.dp
    val focusedBorderWidth = 2.dp
    val contentVerticalPadding = 10.dp
    val contentHorizontalPadding = 12.dp
    val itemSpacing = 10.dp
    val titleStartPadding = 4.dp
    val titleBottomPadding = 6.dp

    object Depth {
        val offsetX = 3.5.dp
        val offsetY = 4.dp
    }
}