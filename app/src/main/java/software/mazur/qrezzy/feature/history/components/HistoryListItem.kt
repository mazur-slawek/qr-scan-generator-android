package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.extensions.label
import software.mazur.qrezzy.core.designsystem.extensions.ui
import software.mazur.qrezzy.core.designsystem.theme.Error
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryListItem(qr: Qr, isDeleteModeEnabled: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues.Zero,
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Surface, contentColor = qr.type.ui.containerColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(HistoryListItemDefaults.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = qr.type.ui.icon,
                contentDescription = stringResource(qr.type.ui.labelResId),
                tint = TextPrimary,
                modifier = Modifier
                    .border(
                        width = HistoryListItemDefaults.Icon.borderWidth,
                        color = qr.type.ui.containerColor,
                        shape = ShapeDefaults.Small,
                    )
                    .shadow(elevation = HistoryListItemDefaults.Icon.elevation, shape = ShapeDefaults.Small)
                    .background(color = qr.type.ui.contentColor, shape = ShapeDefaults.Small)
                    .padding(HistoryListItemDefaults.Icon.padding),
            )

            Spacer(modifier = Modifier.width(HistoryListItemDefaults.iconTextSpacing))

            Column(modifier = Modifier.weight(HistoryListItemDefaults.TEXT_WEIGHT)) {
                Text(
                    text = qr.label,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = HistoryListItemDefaults.TITLE_MAX_LINES,
                )

                Spacer(modifier = Modifier.height(HistoryListItemDefaults.textSpacing))

                Text(
                    text = qr.subtitle(),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = HistoryListItemDefaults.SUBTITLE_MAX_LINES,
                )
            }

            HistoryTrailingIcon(isDeleteModeEnabled = isDeleteModeEnabled, isSelected = isSelected)
        }
    }

}

@Composable
private fun HistoryTrailingIcon(isDeleteModeEnabled: Boolean, isSelected: Boolean) {
    if (isDeleteModeEnabled) {
        Icon(
            imageVector = if (isSelected) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
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
private fun Qr.subtitle(): String = "${source.toDisplayName()} • ${createdAt.toHistoryTime()}"

@Composable
private fun QrSource.toDisplayName(): String =
    when (this) {
        QrSource.GENERATED -> stringResource(R.string.history_source_generated)
        QrSource.SCANNED   -> stringResource(R.string.history_source_scanned)
    }

private fun Long.toHistoryTime(): String =
    SimpleDateFormat(HistoryListItemDefaults.Date.TIME_FORMAT, Locale.getDefault()).format(Date(this))

private object HistoryListItemDefaults {
    val contentPadding = 16.dp
    val iconTextSpacing = 16.dp
    val textSpacing = 2.dp
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