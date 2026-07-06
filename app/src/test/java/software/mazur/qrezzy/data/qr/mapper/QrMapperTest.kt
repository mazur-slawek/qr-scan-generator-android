package software.mazur.qrezzy.data.qr.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.data.qr.local.QrEntity
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.test.createQr

class QrMapperTest {
    @Test
    fun `should map qr entity to domain`() {
        val entity = QrEntity(
            id = 7L,
            source = QrSource.SCANNED,
            type = QrType.URL,
            content = "https://qrezzy.app",
            createdAt = 123456789L,
            isFavorite = true,
            qrColor = 0xFF111111,
            backgroundColor = 0xFFFFFFFF,
            patternStyle = QrPatternStyle.ROUNDED,
            errorCorrection = QrErrorCorrection.HIGH
        )
        val result = entity.toDomain()
        assertEquals(
            createQr(
                id = 7L,
                source = QrSource.SCANNED,
                type = QrType.URL,
                content = "https://qrezzy.app",
                createdAt = 123456789L,
                isFavorite = true,
                style = QrStyle(
                    qrColor = 0xFF111111,
                    backgroundColor = 0xFFFFFFFF,
                    patternStyle = QrPatternStyle.ROUNDED,
                    errorCorrection = QrErrorCorrection.HIGH
                )
            ),
            result
        )
    }

    @Test
    fun `should map qr domain to entity`() {
        val qr = createQr(
            id = 9L,
            source = QrSource.GENERATED,
            type = QrType.WIFI,
            content = "WIFI:T:WPA;S:QREZZY;P:password;H:false;;",
            createdAt = 987654321L,
            isFavorite = true,
            style = QrStyle(
                qrColor = 0xFF222222,
                backgroundColor = 0xFFFFFFFF,
                patternStyle = QrPatternStyle.DOTS,
                errorCorrection = QrErrorCorrection.QUARTILE
            )
        )
        val result = qr.toEntity()
        assertEquals(
            QrEntity(
                id = 9L,
                source = QrSource.GENERATED,
                type = QrType.WIFI,
                content = "WIFI:T:WPA;S:QREZZY;P:password;H:false;;",
                createdAt = 987654321L,
                isFavorite = true,
                qrColor = 0xFF222222,
                backgroundColor = 0xFFFFFFFF,
                patternStyle = QrPatternStyle.DOTS,
                errorCorrection = QrErrorCorrection.QUARTILE
            ),
            result
        )
    }
}
