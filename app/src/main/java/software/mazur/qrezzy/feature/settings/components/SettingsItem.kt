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
                .height(SettingsItemDefaults.height)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = SettingsItemDefaults.horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .size(SettingsItemDefaults.iconSize)
                    .background(color = iconBackgroundColor.copy(alpha = 0.2f), shape = ShapeDefaults.Small)
                    .border(width = 1.dp, color = iconBackgroundColor, shape = ShapeDefaults.Small)
                    .padding(5.dp),
            )

            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = SettingsItemDefaults.titleStartPadding),
            )

            value?.let {
                Text(
                    text = it,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = SettingsItemDefaults.valueEndPadding),
                )
            }

            trailing?.invoke()

            if (onClick != null) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(SettingsItemDefaults.arrowSize),
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = SettingsItemDefaults.dividerStartPadding),
                thickness = SettingsItemDefaults.dividerThickness,
                color = TextSecondary.copy(alpha = SettingsItemDefaults.DIVIDER_ALPHA),
            )
        }
    }
}

@Immutable
private object SettingsItemDefaults {
    val height = 52.dp
    val iconSize = 30.dp
    val arrowSize = 24.dp
    val horizontalPadding = 14.dp
    val titleStartPadding = 14.dp
    val valueEndPadding = 8.dp
    val dividerStartPadding = 55.dp
    val dividerThickness = 1.dp
    const val DIVIDER_ALPHA = 0.12f
}