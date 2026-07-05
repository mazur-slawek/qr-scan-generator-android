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
import androidx.compose.material.icons.filled.OpenInNew
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrType

@Composable
fun QrezzyQrInfoContent(qr: Qr, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val rows = remember(qr) { qr.toInfoRows() }

    QrezzyListSection(title = stringResource(R.string.qr_details_section_details)) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = QrezzyQrInfoContentDefaults.Container.borderWidth,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = ShapeDefaults.Medium
                )
                .background(color = MaterialTheme.colorScheme.surface, shape = ShapeDefaults.Medium)
                .padding(QrezzyQrInfoContentDefaults.Container.padding)
        ) {
            rows.forEachIndexed { index, row ->
                QrezzyQrInfoRow(
                    row = row,
                    onCopyClick = { clipboardManager.setText(AnnotatedString(row.value)) },
                    onOpenLinkClick = { uriHandler.openUri(row.value.toBrowserUrl()) }
                )

                if (index != rows.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = QrezzyQrInfoContentDefaults.Divider.verticalPadding),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun QrezzyQrInfoRow(row: QrInfoRow, onCopyClick: () -> Unit, onOpenLinkClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(QrezzyQrInfoContentDefaults.Content.WEIGHT)) {
            Text(
                text = stringResource(row.labelResId),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = row.value,
                maxLines = QrezzyQrInfoContentDefaults.Value.MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (row.isCopyEnabled) {
            Spacer(modifier = Modifier.width(QrezzyQrInfoContentDefaults.ActionButton.spacing))
            IconButton(
                onClick = onCopyClick,
                modifier = Modifier.size(QrezzyQrInfoContentDefaults.ActionButton.size)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CopyAll,
                    contentDescription = stringResource(R.string.qr_details_copy_content),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (row.isOpenLinkEnabled) {
            Spacer(modifier = Modifier.width(QrezzyQrInfoContentDefaults.ActionButton.spacing))
            IconButton(
                onClick = onOpenLinkClick,
                modifier = Modifier.size(QrezzyQrInfoContentDefaults.ActionButton.size)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = stringResource(R.string.qr_details_open_link),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun Qr.toInfoRows(): List<QrInfoRow> = when (type) {
    QrType.TEXT ->
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_text, value = content))

    QrType.URL ->
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_url, value = content, isOpenLinkEnabled = true))

    QrType.PHONE ->
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_phone, value = content.removePrefix("tel:")))

    QrType.EMAIL -> parseEmailRows(content)
    QrType.WIFI -> parseWifiRows(content)
    QrType.CONTACT -> parseContactRows(content)
    QrType.SMS -> parseSmsRows(content)
    QrType.GEO_LOCATION -> parseGeoLocationRows(content)
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
            QrInfoRow(
                labelResId = R.string.qr_details_field_email_address,
                value = content.removePrefix("mailto:")
            )
        )
    }
    val email = content.extractBetween("TO:", ";")
    val subject = content.extractBetween("SUB:", ";")
    val body = content.extractBetween("BODY:", ";;")

    return listOfNotNull(
        QrInfoRow(labelResId = R.string.qr_details_field_email_address, value = email).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_email_subject, value = subject).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_email_message, value = body).takeIfValueNotBlank()
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
        QrInfoRow(labelResId = R.string.qr_details_field_contact_company, value = company).takeIfValueNotBlank()
    ).ifEmpty {
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_contact_raw, value = content))
    }
}

private fun parseSmsRows(content: String): List<QrInfoRow> {
    val normalizedContent = content.trim()
    if (normalizedContent.startsWith("SMSTO:", ignoreCase = true)) {
        val payload = normalizedContent.removePrefixIgnoreCase("SMSTO:")
        val phone = payload.substringBefore(":").trim()
        val message = payload.substringAfter(":", "").trim()

        return listOfNotNull(
            QrInfoRow(labelResId = R.string.qr_details_field_sms_phone, value = phone).takeIfValueNotBlank(),
            QrInfoRow(labelResId = R.string.qr_details_field_sms_message, value = message).takeIfValueNotBlank()
        ).ifEmpty {
            listOf(QrInfoRow(labelResId = R.string.qr_details_field_sms_raw, value = content))
        }
    }
    val payload = normalizedContent.removePrefixIgnoreCase("sms:")
    val phone = payload.substringBefore("?").trim()
    val message = payload
        .substringAfter("body=", "")
        .substringBefore("&")
        .trim()
    return listOfNotNull(
        QrInfoRow(labelResId = R.string.qr_details_field_sms_phone, value = phone).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_sms_message, value = message).takeIfValueNotBlank()
    ).ifEmpty {
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_sms_raw, value = content))
    }
}

private fun parseGeoLocationRows(content: String): List<QrInfoRow> {
    val payload = content
        .trim()
        .removePrefixIgnoreCase("geo:")
        .substringBefore("?")
    val latitude = payload.substringBefore(",", "").trim()
    val longitude = payload.substringAfter(",", "").trim()

    return listOfNotNull(
        QrInfoRow(labelResId = R.string.qr_details_field_geo_latitude, value = latitude).takeIfValueNotBlank(),
        QrInfoRow(labelResId = R.string.qr_details_field_geo_longitude, value = longitude).takeIfValueNotBlank()
    ).ifEmpty {
        listOf(QrInfoRow(labelResId = R.string.qr_details_field_geo_raw, value = content))
    }
}

private fun String.removePrefixIgnoreCase(prefix: String): String = if (startsWith(prefix, ignoreCase = true)) drop(prefix.length) else this

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

private fun List<String>.findValue(prefix: String): String = firstOrNull { line -> line.startsWith(prefix, ignoreCase = true) }
    ?.substringAfter(prefix)
    ?.trim()
    .orEmpty()

private fun String.extractBetween(start: String, end: String): String = substringAfter(start, "")
    .substringBefore(end, "")
    .trim()

private fun String.toBrowserUrl(): String {
    val trimmedValue = trim()

    return if (
        trimmedValue.startsWith("http://", ignoreCase = true) ||
        trimmedValue.startsWith("https://", ignoreCase = true)
    ) {
        trimmedValue
    } else {
        "https://$trimmedValue"
    }
}

private fun String?.orDash(): String = orEmpty().ifBlank { QrezzyQrInfoContentDefaults.EMPTY_VALUE }

private fun QrInfoRow.takeIfValueNotBlank(): QrInfoRow? = takeIf { row -> row.value.isNotBlank() }

private data class QrInfoRow(
    @param:StringRes val labelResId: Int,
    val value: String,
    val isCopyEnabled: Boolean = true,
    val isOpenLinkEnabled: Boolean = false
)

private object QrezzyQrInfoContentDefaults {
    const val EMPTY_VALUE = "-"

    object Container {
        val borderWidth = 0.5.dp
        val padding = 12.dp
    }

    object Divider {
        val verticalPadding = 8.dp
    }

    object Content {
        const val WEIGHT = 1f
    }

    object ActionButton {
        val size = 30.dp
        val spacing = 8.dp
    }

    object Value {
        const val MAX_LINES = 2
    }
}
