package software.mazur.qrezzy.feature.generator.model

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

    SmsPhone,
    SmsMessage,

    GeoLatitude,
    GeoLongitude
}
