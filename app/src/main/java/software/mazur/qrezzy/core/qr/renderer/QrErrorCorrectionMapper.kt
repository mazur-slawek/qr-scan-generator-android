package software.mazur.qrezzy.core.qr.renderer

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection

internal fun QrErrorCorrection.toZxingLevel(): ErrorCorrectionLevel = when (this) {
    QrErrorCorrection.LOW -> ErrorCorrectionLevel.L
    QrErrorCorrection.MEDIUM -> ErrorCorrectionLevel.M
    QrErrorCorrection.QUARTILE -> ErrorCorrectionLevel.Q
    QrErrorCorrection.HIGH -> ErrorCorrectionLevel.H
}
