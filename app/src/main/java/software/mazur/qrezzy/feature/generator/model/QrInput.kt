package software.mazur.qrezzy.feature.generator.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface QrInput {
    data class Text(
        val text: String = "",
    ) : QrInput

    data class Url(
        val url: String = "",
    ) : QrInput

    data class Wifi(
        val ssid: String = "",
        val password: String = "",
        val encryption: WifiEncryption = WifiEncryption.WPA,
        val hidden: Boolean = false,
    ) : QrInput

    data class Contact(
        val firstName: String = "",
        val lastName: String = "",
        val phone: String = "",
        val email: String = "",
        val company: String = "",
    ) : QrInput

    data class Email(
        val email: String = "",
        val subject: String = "",
        val body: String = "",
    ) : QrInput

    data class Phone(
        val phoneNumber: String = "",
    ) : QrInput
}

enum class WifiEncryption {
    WPA,
    WEP,
    NONE,
}

fun QrInput.isSameTypeAs(other: QrInput): Boolean {
    return this::class == other::class
}

enum class QrInputField {
    Text,
    Url,
    Phone,

    EmailAddress,
    EmailSubject,
    EmailBody,

    WifiSsid,
    WifiPassword,

    ContactFirstName,
    ContactLastName,
    ContactPhone,
    ContactEmail,
    ContactCompany,
}