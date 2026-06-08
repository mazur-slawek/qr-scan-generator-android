package software.mazur.qrezzy.feature.generator.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface QrType {
    data class Text(
        val text: String = "",
    ) : QrType

    data class Url(
        val url: String = "",
    ) : QrType

    data class Wifi(
        val ssid: String = "",
        val password: String = "",
        val encryption: WifiEncryption = WifiEncryption.WPA,
        val hidden: Boolean = false,
    ) : QrType

    data class Contact(
        val firstName: String = "",
        val lastName: String = "",
        val phone: String = "",
        val email: String = "",
        val company: String = "",
    ) : QrType

    data class Email(
        val email: String = "",
        val subject: String = "",
        val body: String = "",
    ) : QrType

    data class Phone(
        val phoneNumber: String = "",
    ) : QrType
}

enum class WifiEncryption {
    WPA,
    WEP,
    NONE,
}

fun QrType.isSameTypeAs(other: QrType): Boolean {
    return this::class == other::class
}