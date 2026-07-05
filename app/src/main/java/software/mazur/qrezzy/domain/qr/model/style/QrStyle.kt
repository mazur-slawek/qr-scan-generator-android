package software.mazur.qrezzy.domain.qr.model.style

data class QrStyle(
    val qrColor: Long = QrStyleDefaults.QR_COLOR,
    val backgroundColor: Long = QrStyleDefaults.BACKGROUND_COLOR,
    val patternStyle: QrPatternStyle = QrPatternStyle.SQUARE,
    val errorCorrection: QrErrorCorrection = QrErrorCorrection.MEDIUM
)

object QrStyleDefaults {
    const val QR_COLOR = 0xFF000000
    const val BACKGROUND_COLOR = 0xFFFFFFFF
}
