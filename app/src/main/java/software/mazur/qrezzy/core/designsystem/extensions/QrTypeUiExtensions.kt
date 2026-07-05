package software.mazur.qrezzy.core.designsystem.extensions

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.InsertLink
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.core.extensions.removePrefixIgnoreCase
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrType

@Immutable
data class QrTypeUi(@get:StringRes val labelResId: Int, val icon: ImageVector, val containerColor: Color, val contentColor: Color)

val QrType.ui: QrTypeUi
    get() =
        when (this) {
            QrType.TEXT ->
                QrTypeUi(
                    labelResId = R.string.qr_type_text,
                    icon = Icons.Outlined.Textsms,
                    containerColor = QrezzyYellowDark,
                    contentColor = QrezzyYellow
                )

            QrType.URL ->
                QrTypeUi(
                    labelResId = R.string.qr_type_url,
                    icon = Icons.Outlined.InsertLink,
                    containerColor = QrezzyMintDark,
                    contentColor = QrezzyMint
                )

            QrType.WIFI ->
                QrTypeUi(
                    labelResId = R.string.qr_type_wifi,
                    icon = Icons.Outlined.Wifi,
                    containerColor = QrezzyPurpleDark,
                    contentColor = QrezzyPurple
                )

            QrType.CONTACT ->
                QrTypeUi(
                    labelResId = R.string.qr_type_contact,
                    icon = Icons.Outlined.AccountCircle,
                    containerColor = QrezzyYellowDark,
                    contentColor = QrezzyYellow
                )

            QrType.EMAIL ->
                QrTypeUi(
                    labelResId = R.string.qr_type_email,
                    icon = Icons.Outlined.AlternateEmail,
                    containerColor = QrezzyPinkDark,
                    contentColor = QrezzyPink
                )

            QrType.PHONE ->
                QrTypeUi(
                    labelResId = R.string.qr_type_phone,
                    icon = Icons.Outlined.Smartphone,
                    containerColor = QrezzyPurpleDark,
                    contentColor = QrezzyPurple
                )

            QrType.SMS -> QrTypeUi(
                labelResId = R.string.qr_type_sms,
                icon = Icons.Outlined.Sms,
                containerColor = QrezzyPinkDark,
                contentColor = QrezzyPink
            )

            QrType.GEO_LOCATION -> QrTypeUi(
                labelResId = R.string.qr_type_geo_location,
                icon = Icons.Outlined.LocationOn,
                containerColor = QrezzyMintDark,
                contentColor = QrezzyMint
            )
        }
val Qr.label: String
    get() =
        when (type) {
            QrType.TEXT -> content.toReadableTextLabel()
            QrType.URL -> content.toReadableUrlLabel()
            QrType.WIFI -> content.toReadableWifiLabel()
            QrType.CONTACT -> content.toReadableContactLabel()
            QrType.EMAIL -> content.toReadableEmailLabel()
            QrType.PHONE -> content.toReadablePhoneLabel()
            QrType.SMS -> content.toReadableSmsLabel()
            QrType.GEO_LOCATION -> content.toReadableGeoLocationLabel()
        }

private fun String.toReadableTextLabel(): String = trim()
    .ifBlank { QrTypeUiDefaults.TEXT_FALLBACK_LABEL }

private fun String.toReadableUrlLabel(): String = trim()
    .removePrefix("https://")
    .removePrefix("http://")
    .substringBefore("/")
    .ifBlank { QrTypeUiDefaults.URL_FALLBACK_LABEL }

private fun String.toReadableWifiLabel(): String = substringAfter("S:", "")
    .substringBefore(";")
    .trim()
    .ifBlank { QrTypeUiDefaults.WIFI_FALLBACK_LABEL }

private fun String.toReadableContactLabel(): String = lineSequence()
    .firstOrNull { line -> line.startsWith("FN:", ignoreCase = true) }
    ?.substringAfter("FN:")
    ?.trim()
    ?.takeIf { value -> value.isNotBlank() }
    ?: QrTypeUiDefaults.CONTACT_FALLBACK_LABEL

private fun String.toReadableEmailLabel(): String {
    if (startsWith("mailto:", ignoreCase = true)) {
        return removePrefix("mailto:")
            .substringBefore("?")
            .trim()
            .ifBlank { QrTypeUiDefaults.EMAIL_FALLBACK_LABEL }
    }

    return substringAfter("TO:", "")
        .substringBefore(";")
        .trim()
        .ifBlank { QrTypeUiDefaults.EMAIL_FALLBACK_LABEL }
}

private fun String.toReadablePhoneLabel(): String = removePrefix("tel:")
    .trim()
    .ifBlank { QrTypeUiDefaults.PHONE_FALLBACK_LABEL }

private fun String.toReadableSmsLabel(): String {
    val normalizedContent = trim()
    if (normalizedContent.startsWith("SMSTO:", ignoreCase = true)) {
        val payload = normalizedContent.removePrefixIgnoreCase("SMSTO:")
        return payload.substringBefore(":").trim().ifBlank { this }
    }
    val payload = normalizedContent.removePrefixIgnoreCase("sms:")
    return payload.substringBefore("?").trim().ifBlank { this }
}

private fun String.toReadableGeoLocationLabel(): String {
    val payload = trim()
        .removePrefixIgnoreCase("geo:")
        .substringBefore("?")
    val latitude = payload.substringBefore(",", "").trim()
    val longitude = payload.substringAfter(",", "").trim()
    return if (latitude.isNotBlank() && longitude.isNotBlank()) "$latitude, $longitude" else this
}

private object QrTypeUiDefaults {
    const val TEXT_FALLBACK_LABEL = "Text QR"
    const val URL_FALLBACK_LABEL = "Website"
    const val WIFI_FALLBACK_LABEL = "Wi-Fi Network"
    const val CONTACT_FALLBACK_LABEL = "Contact"
    const val EMAIL_FALLBACK_LABEL = "Email"
    const val PHONE_FALLBACK_LABEL = "Phone"
}
