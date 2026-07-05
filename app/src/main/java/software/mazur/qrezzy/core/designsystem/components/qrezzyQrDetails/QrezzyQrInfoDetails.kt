package software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.extensions.ui
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.domain.qr.model.Qr
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun QrezzyQrInfoDetails(qr: Qr, modifier: Modifier = Modifier) {
    val qrTypeUi = qr.type.ui
    QrezzyListSection(title = stringResource(R.string.qr_details_section_content)) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = QrezzyQrInfoDetailsDefaults.Container.borderWidth,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = ShapeDefaults.Medium,
                )
                .background(color = MaterialTheme.colorScheme.surface, shape = ShapeDefaults.Medium)
                .padding(QrezzyQrInfoDetailsDefaults.Container.padding),
        ) {
            QrezzyQrInfoDetailsRow(
                icon = qrTypeUi.icon,
                iconBorderColor = qrTypeUi.containerColor,
                iconBackgroundColor = qrTypeUi.contentColor,
                label = stringResource(R.string.qr_details_type),
                value = stringResource(qrTypeUi.labelResId),
                contentDescription = stringResource(qrTypeUi.labelResId),
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = QrezzyQrInfoDetailsDefaults.Divider.verticalPadding),
                color = MaterialTheme.colorScheme.surfaceContainer,
            )
            QrezzyQrInfoDetailsRow(
                icon = Icons.Default.MoreTime,
                iconBorderColor = QrezzyPurpleDark,
                iconBackgroundColor = QrezzyPurple,
                label = stringResource(R.string.qr_details_scanned),
                value = qr.createdAt.toQrDateTime(),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun QrezzyQrInfoDetailsRow(
    icon: ImageVector,
    iconBorderColor: Color,
    iconBackgroundColor: Color,
    label: String,
    value: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        QrezzyQrInfoDetailsIcon(
            icon = icon,
            iconBorderColor = iconBorderColor,
            backgroundColor = iconBackgroundColor,
            contentDescription = contentDescription,
        )
        Spacer(modifier = Modifier.width(QrezzyQrInfoDetailsDefaults.Container.padding))
        Column {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelMedium,
            )
            Spacer(modifier = Modifier.height(QrezzyQrInfoDetailsDefaults.Text.spacing))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun QrezzyQrInfoDetailsIcon(
    icon: ImageVector,
    iconBorderColor: Color,
    backgroundColor: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.inverseOnSurface,
        modifier = modifier
            .border(
                color = iconBorderColor,
                shape = ShapeDefaults.Small,
                width = QrezzyQrInfoDetailsDefaults.Icon.borderWidth
            )
            .background(color = backgroundColor, shape = ShapeDefaults.Small)
            .padding(QrezzyQrInfoDetailsDefaults.Icon.padding)
    )
}

private fun Long.toQrDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern(QrezzyQrInfoDetailsDefaults.DATE_TIME_PATTERN)
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(formatter)
}

private object QrezzyQrInfoDetailsDefaults {
    const val DATE_TIME_PATTERN = "dd.MM.yyyy • HH:mm"

    object Container {
        val borderWidth = 0.5.dp
        val padding = 12.dp
    }

    object Divider {
        val verticalPadding = 8.dp
    }

    object Text {
        val spacing = 3.dp
    }

    object Icon {
        val borderWidth = 1.5.dp
        val padding = 12.dp
    }
}