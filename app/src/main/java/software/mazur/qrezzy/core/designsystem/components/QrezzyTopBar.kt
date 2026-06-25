package software.mazur.qrezzy.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButtonDefaults.Content
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun QrezzyTopBar(
    @StringRes titleResId: Int,
    @StringRes subtitleResId: Int? = null,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(QrezzyTopBarDefaults.height),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBackClick != null) {
            QrezzyTopBarButton(onClick = onBackClick, icon = Icons.Outlined.ArrowBack)
            Spacer(modifier = Modifier.width(Content.padding))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(titleResId),
                    maxLines = QrezzyTopBarDefaults.TITLE_MAX_LINES,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.width(7.dp))
                Icon(
                    painter = painterResource(R.drawable.qrezzy_sparkles),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
            subtitleResId?.let {
                Text(
                    text = stringResource(subtitleResId),
                    maxLines = QrezzyTopBarDefaults.TITLE_MAX_LINES,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Spacer(modifier = Modifier.width(Content.padding))
        content?.let {
            Row(
                modifier = Modifier.height(QrezzyTopBarDefaults.height),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                it()
            }
        }
    }
}

private object QrezzyTopBarDefaults {
    val height = 60.dp
    const val TITLE_MAX_LINES = 1
}

@Composable
fun QrezzyTopBarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String? = null,
    enabled: Boolean = true,
    iconTint: Color = TextPrimary,
    containerColor: Color = Surface,
    depthColor: Color = QrezzyMint,
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .alpha(if (enabled) 1f else 0.4f)
            .height(QrezzyTopBarButtonDefaults.Container.size),
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    top = QrezzyTopBarButtonDefaults.Depth.offsetY,
                    start = QrezzyTopBarButtonDefaults.Depth.offsetX,
                )
                .background(color = depthColor, shape = ShapeDefaults.Medium)
                .border(
                    width = QrezzyTopBarButtonDefaults.Depth.borderWidth,
                    color = BorderPrimary,
                    shape = ShapeDefaults.Medium,
                )
                .align(Alignment.BottomEnd),
        )
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = ShapeDefaults.Medium,
            border = BorderStroke(width = QrezzyTopBarButtonDefaults.Border.width, color = BorderPrimary),
            contentPadding = PaddingValues(horizontal = Content.padding),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = TextPrimary,
                disabledContainerColor = containerColor,
                disabledContentColor = TextPrimary,
            ),
            modifier = Modifier
                .then(
                    if (text == null) {
                        Modifier.size(QrezzyTopBarButtonDefaults.Container.size)
                    } else {
                        Modifier.height(QrezzyTopBarButtonDefaults.Container.size)
                    }
                )
                .padding(
                    end = QrezzyTopBarButtonDefaults.Depth.offsetY,
                    bottom = QrezzyTopBarButtonDefaults.Depth.offsetX,
                )
                .defaultMinSize(
                    minWidth = QrezzyTopBarButtonDefaults.Container.size,
                    minHeight = QrezzyTopBarButtonDefaults.Container.size,
                )
                .align(Alignment.TopStart)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                text?.let {
                    Text(
                        text = it,
                        color = TextPrimary,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = QrezzyTopBarButtonDefaults.Text.MAX_LINES,
                    )
                }
                if (text != null && icon != null) Spacer(modifier = Modifier.width(Content.iconTextSpacing))
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(QrezzyTopBarButtonDefaults.Icon.size)
                    )
                }
            }
        }
    }
}

private object QrezzyTopBarButtonDefaults {
    object Container {
        val size = 50.dp
    }

    object Content {
        val padding = 12.dp
        val iconTextSpacing = 8.dp
    }

    object Depth {
        val offsetX = 3.dp
        val offsetY = 2.5.dp
        val borderWidth = 1.dp
    }

    object Border {
        val width = 1.5.dp
    }

    object Icon {
        val size = 24.dp
    }

    object Text {
        const val MAX_LINES = 1
    }
}
