package software.mazur.qrezzy.feature.generator.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.WifiEncryption

class QrInputMapperTest {
    @Test
    fun `should map qr inputs to qr types`() {
        assertEquals(QrType.TEXT, QrInput.Text().toQrType())
        assertEquals(QrType.URL, QrInput.Url().toQrType())
        assertEquals(QrType.PHONE, QrInput.Phone().toQrType())
        assertEquals(QrType.EMAIL, QrInput.Email().toQrType())
        assertEquals(QrType.WIFI, QrInput.Wifi().toQrType())
        assertEquals(QrType.CONTACT, QrInput.Contact().toQrType())
        assertEquals(QrType.SMS, QrInput.Sms().toQrType())
        assertEquals(QrType.GEO_LOCATION, QrInput.GeoLocation().toQrType())
    }

    @Test
    fun `should map text input to trimmed content`() {
        val input = QrInput.Text(text = "  Hello QREZZY  ")
        val result = input.toQrContent()
        assertEquals("Hello QREZZY", result)
    }

    @Test
    fun `should map url input to trimmed content`() {
        val input = QrInput.Url(url = "  https://qrezzy.app  ")
        val result = input.toQrContent()
        assertEquals("https://qrezzy.app", result)
    }

    @Test
    fun `should map phone input to tel content`() {
        val input = QrInput.Phone(phoneNumber = "  +48123456789  ")
        val result = input.toQrContent()
        assertEquals("tel:+48123456789", result)
    }

    @Test
    fun `should return empty content for blank phone input`() {
        val input = QrInput.Phone(phoneNumber = "   ")
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should map email input to MATMSG content`() {
        val input = QrInput.Email(email = " contact@qrezzy.app ", subject = " Hello ", body = " Body ")
        val result = input.toQrContent()
        assertEquals("MATMSG:TO:contact@qrezzy.app;SUB:Hello;BODY:Body;;", result)
    }

    @Test
    fun `should return empty content for blank email input`() {
        val input = QrInput.Email(email = " ")
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should map wifi input with WPA encryption`() {
        val input = QrInput.Wifi(ssid = " TEST ", password = " password ", encryption = WifiEncryption.WPA, hidden = false)
        val result = input.toQrContent()
        assertEquals("WIFI:T:WPA;S:TEST;P:password;H:false;;", result)
    }

    @Test
    fun `should map wifi input without password for none encryption`() {
        val input = QrInput.Wifi(ssid = "TEST", password = "password", encryption = WifiEncryption.NONE, hidden = true)
        val result = input.toQrContent()
        assertEquals("WIFI:T:nopass;S:TEST;H:true;;", result)
    }

    @Test
    fun `should return empty content for blank wifi ssid`() {
        val input = QrInput.Wifi(ssid = " ")
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should return empty content for empty contact input`() {
        val input = QrInput.Contact()
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should map sms input with message`() {
        val input = QrInput.Sms(phoneNumber = " +48123456789 ", message = " TEST ")
        val result = input.toQrContent()
        assertEquals("sms:+48123456789?body=TEST", result)
    }

    @Test
    fun `should map sms input without message`() {
        val input = QrInput.Sms(phoneNumber = "+48123456789", message = " ")
        val result = input.toQrContent()
        assertEquals("sms:+48123456789", result)
    }

    @Test
    fun `should return empty content for blank sms phone`() {
        val input = QrInput.Sms(phoneNumber = " ")
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should map geo location input`() {
        val input = QrInput.GeoLocation(latitude = " 52.4064 ", longitude = " 16.9252 ")
        val result = input.toQrContent()
        assertEquals("geo:52.4064,16.9252", result)
    }

    @Test
    fun `should return empty content for blank geo location input`() {
        val input = QrInput.GeoLocation(latitude = "52.4064", longitude = " ")
        val result = input.toQrContent()
        assertEquals("", result)
    }
}
