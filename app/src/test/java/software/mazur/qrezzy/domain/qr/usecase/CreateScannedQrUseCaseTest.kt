package software.mazur.qrezzy.domain.qr.usecase

import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.test.FakeTimeProvider
import software.mazur.qrezzy.test.createQr

class CreateScannedQrUseCaseTest {
    private fun createUseCase(now: Long = NOW): CreateScannedQrUseCase = CreateScannedQrUseCase(timeProvider = FakeTimeProvider(now = now))

    @Test
    fun `should trim content`() {
        val useCase = createUseCase()
        val result = useCase("  QREZZY TEST ")
        assertEquals(
            createQr(
                id = 0L,
                content = "QREZZY TEST",
                type = QrType.TEXT,
                source = QrSource.SCANNED,
                createdAt = NOW
            ),
            result
        )
    }

    @Test
    fun `should set source as scanned`() {
        val useCase = createUseCase()
        val result = useCase("QREZZY")
        assertEquals(QrSource.SCANNED, result.source)
    }

    @Test
    fun `should set created at from time provider`() {
        val useCase = createUseCase(now = 987654321L)
        val result = useCase("QREZZY")
        assertEquals(
            createQr(id = 0L, content = "QREZZY", type = QrType.TEXT, source = QrSource.SCANNED, createdAt = 987654321L),
            result
        )
    }

    @Test
    fun `should detect url for http content`() {
        val result = createUseCase()("http://qrezzy.com")
        assertEquals(QrType.URL, result.type)
    }

    @Test
    fun `should detect url for https content`() {
        val result = createUseCase()("https://qrezzy.com")
        assertEquals(QrType.URL, result.type)
    }

    @Test
    fun `should detect email for mailto content`() {
        val result = createUseCase()("mailto:contact@qrezzy.com")
        assertEquals(QrType.EMAIL, result.type)
    }

    @Test
    fun `should detect phone for tel content`() {
        val result = createUseCase()("tel:+48123456789")
        assertEquals(QrType.PHONE, result.type)
    }

    @Test
    fun `should detect wifi for wifi content`() {
        val result = createUseCase()("WIFI:T:WPA;S:QREZZY;P:password;;")
        assertEquals(QrType.WIFI, result.type)
    }

    @Test
    fun `should detect contact for vcard content`() {
        val result = createUseCase()("BEGIN:VCARD\nFN:User Name\nEND:VCARD")
        assertEquals(QrType.CONTACT, result.type)
    }

    @Test
    fun `should detect sms for sms content`() {
        val result = createUseCase()("sms:+48123456789?body=Hello")
        assertEquals(QrType.SMS, result.type)
    }

    @Test
    fun `should detect sms for smsto content`() {
        val result = createUseCase()("smsto:+48123456789:TEST")
        assertEquals(QrType.SMS, result.type)
    }

    @Test
    fun `should detect geo location for geo content`() {
        val result = createUseCase()("geo:52.4064,16.9252")
        assertEquals(QrType.GEO_LOCATION, result.type)
    }

    @Test
    fun `should return text for unknown content`() {
        val result = createUseCase()("regular text")
        assertEquals(QrType.TEXT, result.type)
    }

    @Test
    fun `should detect email for MATMSG content`() {
        val result = createUseCase()("MATMSG:TO:contact@qrezzy.com;SUB:Hello;BODY:Message;;")
        assertEquals(QrType.EMAIL, result.type)
    }

    @Test
    fun `should detect email for lowercase MATMSG content`() {
        val result = createUseCase()("matmsg:TO:contact@qrezzy.com;SUB:Hello;BODY:Message;;")
        assertEquals(QrType.EMAIL, result.type)
    }

    @Test
    fun `should detect types ignoring prefix case`() {
        val useCase = createUseCase()
        assertEquals(QrType.URL, useCase("HTTPS://qrezzy.com").type)
        assertEquals(QrType.EMAIL, useCase("MAILTO:contact@qrezzy.com").type)
        assertEquals(QrType.PHONE, useCase("TEL:+48123456789").type)
        assertEquals(QrType.WIFI, useCase("wifi:T:WPA;S:QREZZY;P:password;;").type)
        assertEquals(QrType.CONTACT, useCase("begin:vcard\nFN:User Name\nEND:VCARD").type)
        assertEquals(QrType.SMS, useCase("SMS:+48123456789?body=Hello").type)
        assertEquals(QrType.SMS, useCase("SMSTO:+48123456789:Hello").type)
        assertEquals(QrType.GEO_LOCATION, useCase("GEO:52.4064,16.9252").type)
    }

    @Test
    fun `should detect type after trimming content whitespace`() {
        val useCase = createUseCase()
        assertEquals(QrType.URL, useCase("  https://qrezzy.com  ").type)
        assertEquals(QrType.EMAIL, useCase("\nMATMSG:TO:contact@qrezzy.com;SUB:Hello;BODY:Message;;\n").type)
        assertEquals(QrType.WIFI, useCase("\tWIFI:T:WPA;S:QREZZY;P:password;;\t").type)
    }

    @Test
    fun `should return text type and empty content for blank input`() {
        val result = createUseCase()("   ")
        assertEquals(QrType.TEXT, result.type)
        assertEquals("", result.content)
    }

    private companion object {
        const val NOW = 123456789L
    }
}
