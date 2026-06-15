package software.mazur.qrezzy.feature.generator.mapper

import software.mazur.qrezzy.data.history.local.QrHistoryType
import software.mazur.qrezzy.feature.generator.model.QrType

fun QrType.toHistoryType(): QrHistoryType {
    return when (this) {
        is QrType.Text    -> QrHistoryType.TEXT
        is QrType.Url     -> QrHistoryType.URL
        is QrType.Wifi    -> QrHistoryType.WIFI
        is QrType.Contact -> QrHistoryType.CONTACT
        is QrType.Email   -> QrHistoryType.EMAIL
        is QrType.Phone   -> QrHistoryType.PHONE
    }
}

fun QrType.toHistoryTitle(): String {
    return when (this) {
        is QrType.Text    -> text.ifBlank { "Text QR" }
        is QrType.Url     -> url.ifBlank { "URL QR" }
        is QrType.Wifi    -> ssid.ifBlank { "Wi-Fi QR" }
        is QrType.Contact -> listOf(firstName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { "Contact QR" }

        is QrType.Email   -> email.ifBlank { "Email QR" }
        is QrType.Phone   -> phoneNumber.ifBlank { "Phone QR" }
    }
}

fun QrType.toPayloadJson(): String {
    return when (this) {
        is QrType.Text    -> """{"text":"${text.escapeJson()}"}"""
        is QrType.Url     -> """{"url":"${url.escapeJson()}"}"""
        is QrType.Wifi    -> """{"ssid":"${ssid.escapeJson()}","password":"${password.escapeJson()}"}"""
        is QrType.Contact -> """
            {
              "firstName":"${firstName.escapeJson()}",
              "lastName":"${lastName.escapeJson()}",
              "phone":"${phone.escapeJson()}",
              "email":"${email.escapeJson()}",
              "company":"${company.escapeJson()}"
            }
        """.trimIndent()

        is QrType.Email   -> """
            {
              "email":"${email.escapeJson()}",
              "subject":"${subject.escapeJson()}",
              "body":"${body.escapeJson()}"
            }
        """.trimIndent()

        is QrType.Phone   -> """{"phoneNumber":"${phoneNumber.escapeJson()}"}"""
    }
}

private fun String.escapeJson(): String {
    return replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
}