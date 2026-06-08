package software.mazur.qrezzy.feature.generator.mapper

import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.generator.model.WifiEncryption

object QrTypeMapper {
    fun mapToQrContent(type: QrType): String {
        return when (type) {
            is QrType.Text    -> type.text.trim()
            is QrType.Url     -> type.url.trim()
            is QrType.Phone   -> mapPhoneToQrContent(type)
            is QrType.Email   -> mapEmailToQrContent(type)
            is QrType.Wifi    -> mapWifiToQrContent(type)
            is QrType.Contact -> mapContactToQrContent(type)
        }
    }

    private fun mapPhoneToQrContent(type: QrType.Phone): String {
        val phone = type.phoneNumber.trim()
        if (phone.isBlank()) return ""

        return "tel:$phone"
    }

    private fun mapEmailToQrContent(type: QrType.Email): String {
        val email = type.email.trim()
        if (email.isBlank()) return ""

        return buildString {
            append("MATMSG:")
            append("TO:$email;")
            append("SUB:${type.subject.trim()};")
            append("BODY:${type.body.trim()};;")
        }
    }

    private fun mapWifiToQrContent(type: QrType.Wifi): String {
        val ssid = type.ssid.trim()
        val password = type.password.trim()

        if (ssid.isBlank()) return ""

        return buildString {
            append("WIFI:")
            append("T:${type.encryption.toQrValue()};")
            append("S:$ssid;")

            if (type.encryption != WifiEncryption.NONE) {
                append("P:$password;")
            }

            append("H:${type.hidden};;")
        }
    }

    private fun mapContactToQrContent(type: QrType.Contact): String {
        val firstName = type.firstName.trim()
        val lastName = type.lastName.trim()
        val phone = type.phone.trim()
        val email = type.email.trim()
        val company = type.company.trim()
        val hasAnyContactValue = listOf(
            firstName,
            lastName,
            phone,
            email,
            company,
        ).any { it.isNotBlank() }

        if (!hasAnyContactValue) return ""

        return buildString {
            appendLine("BEGIN:VCARD")
            appendLine("VERSION:3.0")
            appendLine("N:$lastName;$firstName")
            appendLine("FN:$firstName $lastName".trim())

            appendOptionalLine("TEL", phone)
            appendOptionalLine("EMAIL", email)
            appendOptionalLine("ORG", company)

            appendLine("END:VCARD")
        }
    }

    private fun WifiEncryption.toQrValue(): String {
        return when (this) {
            WifiEncryption.WPA  -> "WPA"
            WifiEncryption.WEP  -> "WEP"
            WifiEncryption.NONE -> "nopass"
        }
    }

    private fun StringBuilder.appendOptionalLine(
        key: String,
        value: String,
    ) {
        if (value.isNotBlank()) {
            appendLine("$key:$value")
        }
    }
}