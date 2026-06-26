package software.mazur.qrezzy.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    titleColor: Color = TextPrimary,
    iconTintColor: Color = TextPrimary,
    iconBackgroundColor: Color = QrezzyMintDark,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
) {
    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(SettingsItemDefaults.Container.height)
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                )
                .padding(horizontal = SettingsItemDefaults.Container.horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SettingsItemIcon(
                icon = icon,
                tintColor = iconTintColor,
                backgroundColor = iconBackgroundColor,
            )

            SettingsItemTitle(
                title = title,
                color = titleColor,
                modifier = Modifier
                    .weight(SettingsItemDefaults.Title.WEIGHT)
                    .padding(start = SettingsItemDefaults.Title.startPadding),
            )

            value?.let { text ->
                SettingsItemValue(value = text)
            }

            trailing?.invoke()

            if (onClick != null) {
                SettingsItemArrow()
            }
        }

        if (showDivider) {
            SettingsItemDivider(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = SettingsItemDefaults.Divider.startPadding),
            )
        }
    }
}

@Composable
private fun SettingsItemIcon(
    icon: ImageVector,
    tintColor: Color,
    backgroundColor: Color,
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier
            .size(SettingsItemDefaults.Icon.size)
            .background(
                color = backgroundColor.copy(alpha = SettingsItemDefaults.Icon.BACKGROUND_ALPHA),
                shape = ShapeDefaults.Small,
            )
            .border(
                width = SettingsItemDefaults.Icon.borderWidth,
                color = backgroundColor,
                shape = ShapeDefaults.Small,
            )
            .padding(SettingsItemDefaults.Icon.padding),
    )
}

@Composable
private fun SettingsItemTitle(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
    )
}

@Composable
private fun SettingsItemValue(value: String) {
    Text(
        text = value,
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(end = SettingsItemDefaults.Value.endPadding),
    )
}

@Composable
private fun SettingsItemArrow() {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowRight,
        contentDescription = null,
        tint = TextSecondary,
        modifier = Modifier.size(SettingsItemDefaults.Arrow.size),
    )
}

@Composable
private fun SettingsItemDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = SettingsItemDefaults.Divider.thickness,
        color = TextSecondary.copy(alpha = SettingsItemDefaults.Divider.ALPHA),
    )
}

@Immutable
private object SettingsItemDefaults {
    object Container {
        val height = 52.dp
        val horizontalPadding = 14.dp
    }

    object Icon {
        val size = 30.dp
        val padding = 5.dp
        val borderWidth = 1.dp
        const val BACKGROUND_ALPHA = 0.2f
    }

    object Title {
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
        val startPadding = 55.dp
        val thickness = 1.dp
        const val ALPHA = 0.12f
    }
}