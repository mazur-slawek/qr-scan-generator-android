package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark


@Composable
fun QrezzyListItem(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    subtitle: String? = null,
    value: String? = null,
    iconSize: Dp = QrezzyListItemDefaults.Icon.size,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTintColor: Color? = null,
    iconBackgroundColor: Color = QrezzyMintDark,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
) {
    val hasIcon = icon != null || iconPainter != null
    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick)
                    else Modifier
                )
                .padding(
                    vertical = QrezzyListItemDefaults.Container.verticalPadding,
                    horizontal = QrezzyListItemDefaults.Container.horizontalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QrezzyListItemIcon(
                icon = icon,
                iconPainter = iconPainter,
                size = iconSize,
                tintColor = iconTintColor,
                backgroundColor = iconBackgroundColor,
            )
            if (hasIcon) Spacer(modifier = Modifier.width(QrezzyListItemDefaults.Text.startPadding))
            QrezzyListItemText(
                title = title,
                subtitle = subtitle,
                titleColor = titleColor,
                modifier = Modifier.weight(QrezzyListItemDefaults.Text.WEIGHT),
            )
            Spacer(modifier = Modifier.width(QrezzyListItemDefaults.Text.startPadding))
            value?.let {
                QrezzyListItemValue(value = it)
            }
            trailing?.invoke()
            if (onClick != null) QrezzyListItemArrow()
        }
        if (showDivider) {
            QrezzyListItemDivider(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
private fun QrezzyListItemIcon(
    icon: ImageVector?,
    iconPainter: Painter?,
    size: Dp,
    backgroundColor: Color,
    tintColor: Color? = null,
) {
    val iconModifier = Modifier
        .size(size)
        .background(
            color = backgroundColor.copy(alpha = QrezzyListItemDefaults.Icon.BACKGROUND_ALPHA),
            shape = ShapeDefaults.Small,
        )
        .border(
            width = QrezzyListItemDefaults.Icon.borderWidth,
            color = backgroundColor,
            shape = ShapeDefaults.Small,
        )
        .padding(QrezzyListItemDefaults.Icon.padding)

    when {
        icon != null        -> {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintColor ?: Color.Unspecified,
                modifier = iconModifier,
            )
        }

        iconPainter != null -> {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = tintColor ?: Color.Unspecified,
                modifier = iconModifier,
            )
        }
    }
}

@Composable
private fun QrezzyListItemText(title: String, subtitle: String?, titleColor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        subtitle?.let { text ->
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QrezzyListItemValue(value: String) {
    Text(
        text = value,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(end = QrezzyListItemDefaults.Value.endPadding),
    )
}

@Composable
private fun QrezzyListItemArrow() {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(QrezzyListItemDefaults.Arrow.size),
    )
}

@Composable
private fun QrezzyListItemDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = QrezzyListItemDefaults.Divider.thickness,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = QrezzyListItemDefaults.Divider.ALPHA),
    )
}

@Immutable
private object QrezzyListItemDefaults {
    object Container {
        val verticalPadding = 12.dp
        val horizontalPadding = 16.dp
    }

    object Icon {
        val size = 32.dp
        val padding = 4.dp
        val borderWidth = 1.dp
        const val BACKGROUND_ALPHA = 0.2f
    }

    object Text {
        val startPadding = 14.dp
        const val WEIGHT = 1f
    }

    object Value {
        val endPadding = 8.dp
    }

    object Arrow {
        val size = 24.dp
    }

    object Divider {
        val thickness = 1.dp
        const val ALPHA = 0.12f
    }
}