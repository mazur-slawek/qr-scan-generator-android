package software.mazur.qrezzy.feature.generator.mapper

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField


fun QrInput.toQrType(): QrType {
    return when (this) {
        is QrInput.Text    -> QrType.TEXT
        is QrInput.Url     -> QrType.URL
        is QrInput.Wifi    -> QrType.WIFI
        is QrInput.Contact -> QrType.CONTACT
        is QrInput.Email   -> QrType.EMAIL
        is QrInput.Phone   -> QrType.PHONE
    }
}

fun QrInput.toQrTitle(): String {
    return when (this) {
        is QrInput.Text    -> text.ifBlank { "Text QR" }
        is QrInput.Url     -> url.ifBlank { "URL QR" }
        is QrInput.Wifi    -> ssid.ifBlank { "Wi-Fi QR" }
        is QrInput.Contact -> listOf(firstName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { "Contact QR" }

        is QrInput.Email   -> email.ifBlank { "Email QR" }
        is QrInput.Phone   -> phoneNumber.ifBlank { "Phone QR" }
    }
}

fun QrInput.toQrPayloadJson(): String {
    return when (this) {
        is QrInput.Text    -> """{"text":"${text.escapeJson()}"}"""
        is QrInput.Url     -> """{"url":"${url.escapeJson()}"}"""
        is QrInput.Wifi    -> """{"ssid":"${ssid.escapeJson()}","password":"${password.escapeJson()}"}"""
        is QrInput.Contact -> """
            {
              "firstName":"${firstName.escapeJson()}",
              "lastName":"${lastName.escapeJson()}",
              "phone":"${phone.escapeJson()}",
              "email":"${email.escapeJson()}",
              "company":"${company.escapeJson()}"
            }
        """.trimIndent()

        is QrInput.Email   -> """
            {
              "email":"${email.escapeJson()}",
              "subject":"${subject.escapeJson()}",
              "body":"${body.escapeJson()}"
            }
        """.trimIndent()

        is QrInput.Phone   -> """{"phoneNumber":"${phoneNumber.escapeJson()}"}"""
    }
}

private fun String.escapeJson(): String {
    return replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
}

val QrInputField.label: String
    get() = when (this) {
        QrInputField.Text             -> "Text"
        QrInputField.Url              -> "URL"
        QrInputField.Phone            -> "Phone number"
        QrInputField.EmailAddress     -> "Email"
        QrInputField.EmailSubject     -> "Subject"
        QrInputField.EmailBody        -> "Message"
        QrInputField.WifiSsid         -> "Network name (SSID)"
        QrInputField.WifiPassword     -> "Password"
        QrInputField.ContactFirstName -> "First name"
        QrInputField.ContactLastName  -> "Last name"
        QrInputField.ContactPhone     -> "Phone"
        QrInputField.ContactEmail     -> "Email"
        QrInputField.ContactCompany   -> "Company"
    }
val QrInputField.maxLength: Int
    get() = when (this) {
        QrInputField.Text             -> 500
        QrInputField.Url              -> 2048
        QrInputField.Phone            -> 20
        QrInputField.EmailAddress     -> 254
        QrInputField.EmailSubject     -> 200
        QrInputField.EmailBody        -> 1000
        QrInputField.WifiSsid         -> 32
        QrInputField.WifiPassword     -> 63
        QrInputField.ContactFirstName -> 50
        QrInputField.ContactLastName  -> 50
        QrInputField.ContactPhone     -> 20
        QrInputField.ContactEmail     -> 254
        QrInputField.ContactCompany   -> 100
    }
val QrInputField.keyboardOptions: KeyboardOptions
    get() = when (this) {
        QrInputField.Text,
        QrInputField.EmailSubject,
        QrInputField.EmailBody,
        QrInputField.ContactFirstName,
        QrInputField.ContactLastName,
        QrInputField.ContactCompany,
        QrInputField.WifiSsid     -> KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
        )

        QrInputField.Url          -> KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            autoCorrectEnabled = false,
        )

        QrInputField.Phone,
        QrInputField.ContactPhone -> KeyboardOptions(
            keyboardType = KeyboardType.Phone,
        )

        QrInputField.EmailAddress,
        QrInputField.ContactEmail -> KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrectEnabled = false,
        )

        QrInputField.WifiPassword -> KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrectEnabled = false,
        )
    }