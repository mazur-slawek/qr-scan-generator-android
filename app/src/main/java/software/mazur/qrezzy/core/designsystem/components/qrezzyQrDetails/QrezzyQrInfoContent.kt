package software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrType

@Composable
fun QrezzyQrInfoContent(qr: Qr, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    val rows = remember(qr) { qr.toInfoRows() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = QrezzyQrInfoCardDefaults.Container.borderWidth,
                color = BorderLight,
                shape = ShapeDefaults.Medium,
            )
            .background(color = Surface, shape = ShapeDefaults.Medium)
            .padding(QrezzyQrInfoCardDefaults.Container.padding),
    ) {
        rows.forEachIndexed { index, row ->
            QrezzyQrInfoRow(row = row, onCopyClick = { clipboardManager.setText(AnnotatedString(row.value)) })
            if (index != rows.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = QrezzyQrInfoCardDefaults.Divider.verticalPadding),
                    color = BorderLight,
                )
            }
        }
    }
}

@Composable
private fun QrezzyQrInfoRow(row: QrInfoRow, onCopyClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(row.labelResId),
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = row.value,
                maxLines = QrezzyQrInfoCardDefaults.Value.maxLines,
                overflow = TextOverflow.Ellipsis,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (row.isCopyEnabled) {
            Spacer(modifier = Modifier.width(QrezzyQrInfoCardDefaults.CopyButton.spacing))
            IconButton(onClick = onCopyClick, modifier = Modifier.size(QrezzyQrInfoCardDefaults.CopyButton.size)) {
                Icon(imageVector = Icons.Outlined.CopyAll, contentDescription = null, tint = TextSecondary)
            }
        }
    }
}

private fun Qr.toInfoRows(): List<QrInfoRow> {
    return when (type) {
        QrType.TEXT    -> listOf(QrInfoRow(labelResId = R.string.qr_details_field_text, value = content))
        QrType.URL     -> listOf(QrInfoRow(labelResId = R.string.qr_details_field_url, value = content))
        QrType.PHONE   ->
            listOf(QrInfoRow(labelResId = R.string.qr_details_field_phone, value = content.removePrefix("tel:")))

        QrType.EMAIL   -> parseEmailRows(content)
        QrType.WIFI    -> parseWifiRows(content)
        QrType.CONTACT -> parseContactRows(content)
    }
}

private fun parseWifiRows(content: String): List<QrInfoRow> {
    val values = content.toWifiValues()
    return listOf(
        QrInfoRow(labelResId = R.string.qr_details_field_wifi_network_name, value = values["S"].orEmpty()),
        QrInfoRow(labelResId = R.string.qr_details_field_wifi_security, value = values["T"].orDash()),
        QrInfoRow(labelResId = R.string.qr_details_field_wifi_password, value = values["P"].orDash())
    )
}

private fun parseEmailRows(content: String): List<QrInfoRow> {
    if (content.startsWith("mailto:", ignoreCase = true)) {
        return listOf(
            QrInfoRow(labelResId = R.string.qr_details_field_email_address, value = content.removePrefix("mailto:"))
        )
    }
    val email = content.extractBetween("TO:", ";")
    val subject = content.extractBetween("SUB:", ";")
    val body = content.extractBetween("BODY:", ";;")
    return listOfNotNull(
        QrInfoRow(labelResId = R.string.qr_details_field_email_address, value = email).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_email_subject, value = subject).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_email_message, value = body).takeIfValueNotBlank(),
    )
}

private fun parseContactRows(content: String): List<QrInfoRow> {
    val lines = content.lines()
    val name = lines.findValue("FN:")
    val phone = lines.findValue("TEL:")
    val email = lines.findValue("EMAIL:")
    val company = lines.findValue("ORG:")
    return listOfNotNull(
        QrInfoRow(labelResId = R.string.qr_details_field_contact_name, value = name).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_contact_phone, value = phone).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_contact_email, value = email).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_contact_company, value = company).takeIfValueNotBlank(),
    ).ifEmpty {
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_contact_raw, value = content))
    }
}

private fun String.toWifiValues(): Map<String, String> {
    return removePrefix("WIFI:")
        .split(";")
        .mapNotNull { part ->
            val separatorIndex = part.indexOf(":")
            if (separatorIndex == -1) return@mapNotNull null

            part.take(separatorIndex) to part.drop(separatorIndex + 1)
        }
        .toMap()
}

private fun List<String>.findValue(prefix: String): String {
    return firstOrNull { line -> line.startsWith(prefix, ignoreCase = true) }
        ?.substringAfter(prefix)
        ?.trim()
        .orEmpty()
}

private fun String.extractBetween(start: String, end: String): String {
    return substringAfter(start, "")
        .substringBefore(end, "")
        .trim()
}

private fun String?.orDash(): String {
    return orEmpty().ifBlank { QrezzyQrInfoCardDefaults.EMPTY_VALUE }
}

private fun QrInfoRow.takeIfValueNotBlank(): QrInfoRow? {
    return takeIf { row -> row.value.isNotBlank() }
}

private data class QrInfoRow(
    @param:StringRes val labelResId: Int,
    val value: String,
    val isCopyEnabled: Boolean = true,
)

private object QrezzyQrInfoCardDefaults {
    const val EMPTY_VALUE = "-"

    object Container {
        val borderWidth = 0.5.dp
        val padding = 12.dp
    }

    object Divider {
        val verticalPadding = 8.dp
    }

    object CopyButton {
        val size = 30.dp
        val spacing = 16.dp
    }

    object Value {
        const val maxLines = 2
    }
}