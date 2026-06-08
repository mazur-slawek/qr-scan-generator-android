package software.mazur.qrezzy.feature.generator.presentation.model

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactCompany
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactEmail
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactFirstName
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactLastName
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.ContactPhone
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailAddress
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailBody
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.EmailSubject
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Phone
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Text
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.Url
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.WifiPassword
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField.WifiSsid

enum class QrTypeField {
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

val QrTypeField.label: String
    get() = when (this) {
        Text             -> "Text"
        Url              -> "URL"
        Phone            -> "Phone number"
        EmailAddress     -> "Email"
        EmailSubject     -> "Subject"
        EmailBody        -> "Message"
        WifiSsid         -> "Network name (SSID)"
        WifiPassword     -> "Password"
        ContactFirstName -> "First name"
        ContactLastName  -> "Last name"
        ContactPhone     -> "Phone"
        ContactEmail     -> "Email"
        ContactCompany   -> "Company"
    }
val QrTypeField.maxLength: Int
    get() = when (this) {
        Text             -> 500
        Url              -> 2048
        Phone            -> 20
        EmailAddress     -> 254
        EmailSubject     -> 200
        EmailBody        -> 1000
        WifiSsid         -> 32
        WifiPassword     -> 63
        ContactFirstName -> 50
        ContactLastName  -> 50
        ContactPhone     -> 20
        ContactEmail     -> 254
        ContactCompany   -> 100
    }
val QrTypeField.keyboardOptions: KeyboardOptions
    get() = when (this) {
        Text,
        EmailSubject,
        EmailBody,
        ContactFirstName,
        ContactLastName,
        ContactCompany,
        WifiSsid     -> KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
        )

        Url          -> KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            autoCorrectEnabled = false,
        )

        Phone,
        ContactPhone -> KeyboardOptions(
            keyboardType = KeyboardType.Phone,
        )

        EmailAddress,
        ContactEmail -> KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrectEnabled = false,
        )

        WifiPassword -> KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrectEnabled = false,
        )
    }