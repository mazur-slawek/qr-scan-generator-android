package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.common.TimeProvider
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import javax.inject.Inject

class CreateScannedQrUseCase
@Inject
constructor(private val timeProvider: TimeProvider) {
    operator fun invoke(content: String): Qr {
        val trimmedContent = content.trim()
        return Qr(
            type = detectQrType(trimmedContent),
            source = QrSource.SCANNED,
            content = trimmedContent,
            createdAt = timeProvider.nowMillis()
        )
    }

    private fun detectQrType(content: String): QrType {
        if (content.startsWith("http://", ignoreCase = true)) return QrType.URL
        if (content.startsWith("https://", ignoreCase = true)) return QrType.URL
        if (content.startsWith("mailto:", ignoreCase = true)) return QrType.EMAIL
        if (content.startsWith("MATMSG:", ignoreCase = true)) return QrType.EMAIL
        if (content.startsWith("tel:", ignoreCase = true)) return QrType.PHONE
        if (content.startsWith("WIFI:", ignoreCase = true)) return QrType.WIFI
        if (content.startsWith("BEGIN:VCARD", ignoreCase = true)) return QrType.CONTACT
        if (content.startsWith("sms:", ignoreCase = true)) return QrType.SMS
        if (content.startsWith("smsto:", ignoreCase = true)) return QrType.SMS
        if (content.startsWith("geo:", ignoreCase = true)) return QrType.GEO_LOCATION
        return QrType.TEXT
    }
}
