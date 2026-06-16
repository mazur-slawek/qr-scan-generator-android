package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.Error
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.history.model.QrHistorySource
import software.mazur.qrezzy.feature.generator.model.icon
import software.mazur.qrezzy.feature.generator.model.iconTintColor
import software.mazur.qrezzy.feature.generator.model.iconTintColorDark
import software.mazur.qrezzy.feature.generator.model.label
import software.mazur.qrezzy.feature.history.model.HistoryItemUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryListItem(
    item: HistoryItemUi,
    isDeleteModeEnabled: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = HistoryListItemDefaults.verticalPadding)
            .clip(shape = ShapeDefaults.Medium),
        shape = ShapeDefaults.Medium,
        border = BorderStroke(
            width = HistoryListItemDefaults.borderWidth,
            color = BorderLight,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Surface,
            contentColor = item.qrType.iconTintColorDark,
        ),
        contentPadding = PaddingValues.Zero,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(HistoryListItemDefaults.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.qrType.icon,
                contentDescription = item.qrType.label,
                tint = TextPrimary,
                modifier = Modifier
                    .border(
                        width = HistoryListItemDefaults.Icon.borderWidth,
                        color = item.qrType.iconTintColorDark,
                        shape = ShapeDefaults.Small,
                    )
                    .shadow(
                        elevation = HistoryListItemDefaults.Icon.elevation,
                        shape = ShapeDefaults.Small,
                    )
                    .background(
                        color = item.qrType.iconTintColor,
                        shape = ShapeDefaults.Small,
                    )
                    .padding(HistoryListItemDefaults.Icon.padding),
            )

            Spacer(modifier = Modifier.width(HistoryListItemDefaults.iconTextSpacing))

            Column(modifier = Modifier.weight(HistoryListItemDefaults.TEXT_WEIGHT)) {
                Text(
                    text = item.value,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = HistoryListItemDefaults.TITLE_MAX_LINES,
                )

                Spacer(modifier = Modifier.height(HistoryListItemDefaults.textSpacing))

                Text(
                    text = item.subtitle(),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = HistoryListItemDefaults.SUBTITLE_MAX_LINES,
                )
            }

            HistoryTrailingIcon(
                isDeleteModeEnabled = isDeleteModeEnabled,
                isSelected = isSelected,
            )
        }
    }
}

@Composable
private fun HistoryTrailingIcon(
    isDeleteModeEnabled: Boolean,
    isSelected: Boolean,
) {
    if (isDeleteModeEnabled) {
        Icon(
            imageVector = if (isSelected) {
                Icons.Outlined.CheckCircle
            } else {
                Icons.Outlined.Circle
            },
            tint = if (isSelected) Error else TextSecondary,
            contentDescription = null,
        )
    } else {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            tint = TextSecondary,
            contentDescription = null,
        )
    }
}

@Composable
private fun HistoryItemUi.subtitle(): String {
    return "${source.toDisplayName()} • ${createdAt.toHistoryTime()}"
}

@Composable
private fun QrHistorySource.toDisplayName(): String {
    return when (this) {
        QrHistorySource.GENERATED -> stringResource(R.string.history_source_generated)
        QrHistorySource.SCANNED   -> stringResource(R.string.history_source_scanned)
    }
}

private fun Long.toHistoryTime(): String {
    return SimpleDateFormat(
        HistoryListItemDefaults.Date.TIME_FORMAT,
        Locale.getDefault(),
    ).format(Date(this))
}

private object HistoryListItemDefaults {
    val verticalPadding = 4.dp
    val contentPadding = 16.dp
    val iconTextSpacing = 16.dp
    val textSpacing = 2.dp
    val borderWidth = 1.5.dp
    const val TEXT_WEIGHT = 1f
    const val TITLE_MAX_LINES = 1
    const val SUBTITLE_MAX_LINES = 1

    object Icon {
        val borderWidth = 1.5.dp
        val elevation = 1.5.dp
        val padding = 8.dp
    }

    object Date {
        const val TIME_FORMAT = "HH:mm"
    }
}