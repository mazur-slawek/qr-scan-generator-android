package software.mazur.qrezzy.feature.generator.mapper

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.WifiEncryption

fun QrInput.toQrType(): QrType = when (this) {
    is QrInput.Text -> QrType.TEXT
    is QrInput.Url -> QrType.URL
    is QrInput.Wifi -> QrType.WIFI
    is QrInput.Contact -> QrType.CONTACT
    is QrInput.Email -> QrType.EMAIL
    is QrInput.Phone -> QrType.PHONE
    is QrInput.Sms -> QrType.SMS
    is QrInput.GeoLocation -> QrType.GEO_LOCATION
}

fun QrInput.toQrContent(): String = when (this) {
    is QrInput.Text -> text.trim()
    is QrInput.Url -> url.trim()
    is QrInput.Phone -> toQrContent()
    is QrInput.Email -> toQrContent()
    is QrInput.Wifi -> toQrContent()
    is QrInput.Contact -> toQrContent()
    is QrInput.Sms -> toQrContent()
    is QrInput.GeoLocation -> toQrContent()
}

private fun QrInput.Phone.toQrContent(): String {
    val phone = phoneNumber.trim()
    if (phone.isBlank()) return ""
    return "tel:$phone"
}

private fun QrInput.Email.toQrContent(): String {
    val email = email.trim()
    if (email.isBlank()) return ""

    return buildString {
        append("MATMSG:")
        append("TO:$email;")
        append("SUB:${subject.trim().escapeMatMsg()};")
        append("BODY:${body.trim().escapeMatMsg()};;")
    }
}

private fun QrInput.Wifi.toQrContent(): String {
    val networkName = ssid.trim()
    val networkPassword = password.trim()
    if (networkName.isBlank()) return ""

    return buildString {
        append("WIFI:")
        append("T:${encryption.toQrValue()};")
        append("S:${networkName.escapeWifi()};")
        if (encryption != WifiEncryption.NONE) {
            append("P:${networkPassword.escapeWifi()};")
        }
        append("H:$hidden;;")
    }
}

private fun WifiEncryption.toQrValue(): String = when (this) {
    WifiEncryption.WPA -> "WPA"
    WifiEncryption.WEP -> "WEP"
    WifiEncryption.NONE -> "nopass"
}

private fun QrInput.Contact.toQrContent(): String {
    val contactFirstName = firstName.trim()
    val contactLastName = lastName.trim()
    val contactPhone = phone.trim()
    val contactEmail = email.trim()
    val contactCompany = company.trim()
    val hasAnyContactValue =
        listOf(
            contactFirstName,
            contactLastName,
            contactPhone,
            contactEmail,
            contactCompany
        ).any { value -> value.isNotBlank() }

    if (!hasAnyContactValue) return ""
    val fullName = "$contactFirstName $contactLastName".trim()

    return buildString {
        appendLine("BEGIN:VCARD")
        appendLine("VERSION:3.0")
        appendLine("N:${contactLastName.escapeVCard()};${contactFirstName.escapeVCard()}")
        appendLine("FN:${fullName.escapeVCard()}")
        if (contactPhone.isNotBlank()) appendLine("TEL:${contactPhone.escapeVCard()}")
        if (contactEmail.isNotBlank()) appendLine("EMAIL:${contactEmail.escapeVCard()}")
        if (contactCompany.isNotBlank()) appendLine("ORG:${contactCompany.escapeVCard()}")
        appendLine("END:VCARD")
    }
}

private fun QrInput.GeoLocation.toQrContent(): String {
    val lat = latitude.trim()
    val lng = longitude.trim()

    if (lat.isBlank() || lng.isBlank()) return ""
    return "geo:$lat,$lng"
}

private fun QrInput.Sms.toQrContent(): String {
    val phone = phoneNumber.trim()
    val text = message.trim()

    if (phone.isBlank()) return ""
    return if (text.isBlank()) {
        "sms:$phone"
    } else {
        "sms:$phone?body=${text.encodeUrl()}"
    }
}

private fun String.escapeWifi(): String = buildString(length) {
    this@escapeWifi.forEach { char ->
        when (char) {
            '\\', ';', ',', ':', '"' -> {
                append('\\')
                append(char)
            }

            else -> append(char)
        }
    }
}

private fun String.escapeMatMsg(): String = buildString(length) {
    this@escapeMatMsg.forEach { char ->
        when (char) {
            '\\', ';', ':' -> {
                append('\\')
                append(char)
            }

            else -> append(char)
        }
    }
}

private fun String.escapeVCard(): String = this
    .replace("\\", "\\\\")
    .replace("\n", "\\n")
    .replace(";", "\\;")
    .replace(",", "\\,")

private fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
