package software.mazur.qrezzy.feature.generator.mapper

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import software.mazur.qrezzy.R
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField

val QrInputField.placeholderResId: Int
    @StringRes get() =
        when (this) {
            QrInputField.Text             -> R.string.qr_input_field_placeholder_text
            QrInputField.Url              -> R.string.qr_input_field_placeholder_url
            QrInputField.Phone            -> R.string.qr_input_field_placeholder_phone
            QrInputField.EmailAddress     -> R.string.qr_input_field_placeholder_email_address
            QrInputField.EmailSubject     -> R.string.qr_input_field_placeholder_email_subject
            QrInputField.EmailBody        -> R.string.qr_input_field_placeholder_email_body
            QrInputField.WifiSsid         -> R.string.qr_input_field_placeholder_wifi_ssid
            QrInputField.WifiPassword     -> R.string.qr_input_field_placeholder_wifi_password
            QrInputField.ContactFirstName -> R.string.qr_input_field_placeholder_contact_first_name
            QrInputField.ContactLastName  -> R.string.qr_input_field_placeholder_contact_last_name
            QrInputField.ContactPhone     -> R.string.qr_input_field_placeholder_contact_phone
            QrInputField.ContactEmail     -> R.string.qr_input_field_placeholder_contact_email
            QrInputField.ContactCompany   -> R.string.qr_input_field_placeholder_contact_company
            QrInputField.SmsPhone         -> R.string.qr_input_field_placeholder_sms_phone
            QrInputField.SmsMessage       -> R.string.qr_input_field_placeholder_sms_message
            QrInputField.GeoLatitude      -> R.string.qr_input_field_placeholder_geo_latitude
            QrInputField.GeoLongitude     -> R.string.qr_input_field_placeholder_geo_longitude
        }
val QrInputField.maxLength: Int
    get() =
        when (this) {
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
            QrInputField.SmsPhone         -> 20
            QrInputField.SmsMessage       -> 500
            QrInputField.GeoLatitude      -> 20
            QrInputField.GeoLongitude     -> 20
        }
val QrInputField.keyboardOptions: KeyboardOptions
    get() =
        when (this) {
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

            QrInputField.SmsPhone     -> KeyboardOptions(keyboardType = KeyboardType.Phone)
            QrInputField.SmsMessage   -> KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            )

            QrInputField.GeoLatitude,
            QrInputField.GeoLongitude -> KeyboardOptions(keyboardType = KeyboardType.Decimal)
        }
val QrInput.fields: Set<QrInputField>
    get() =
        when (this) {
            is QrInput.Text        -> setOf(QrInputField.Text)
            is QrInput.Url         -> setOf(QrInputField.Url)
            is QrInput.Phone       -> setOf(QrInputField.Phone)
            is QrInput.Email       -> setOf(
                QrInputField.EmailAddress,
                QrInputField.EmailSubject,
                QrInputField.EmailBody
            )

            is QrInput.Wifi        -> setOf(QrInputField.WifiSsid, QrInputField.WifiPassword)
            is QrInput.Contact     -> setOf(
                QrInputField.ContactFirstName,
                QrInputField.ContactLastName,
                QrInputField.ContactPhone,
                QrInputField.ContactEmail,
                QrInputField.ContactCompany
            )

            is QrInput.Sms         -> setOf(QrInputField.SmsPhone, QrInputField.SmsMessage)
            is QrInput.GeoLocation -> setOf(QrInputField.GeoLatitude, QrInputField.GeoLongitude)
        }
