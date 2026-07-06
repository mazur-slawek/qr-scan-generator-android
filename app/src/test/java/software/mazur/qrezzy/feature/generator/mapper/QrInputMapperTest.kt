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

    @Test
    fun `should map wifi input with WEP encryption`() {
        val input = QrInput.Wifi(ssid = " Home ", password = " 12345 ", encryption = WifiEncryption.WEP, hidden = false)
        val result = input.toQrContent()
        assertEquals("WIFI:T:WEP;S:Home;P:12345;H:false;;", result)
    }

    @Test
    fun `should map wifi input with NONE encryption without password`() {
        val input = QrInput.Wifi(ssid = " Net work ", password = " ignored ", encryption = WifiEncryption.NONE, hidden = false)
        val result = input.toQrContent()
        assertEquals("WIFI:T:nopass;S:Net work;H:false;;", result)
    }

    @Test
    fun `should escape special characters in wifi content`() {
        val input =
            QrInput.Wifi(ssid = " Home;WiFi:Main ", password = """ pa\ss;word:123 """, encryption = WifiEncryption.WPA, hidden = true)
        val result = input.toQrContent()
        assertEquals("""WIFI:T:WPA;S:Home\;WiFi\:Main;P:pa\\ss\;word\:123;H:true;;""", result)
    }

    @Test
    fun `should map contact input with first name and last name`() {
        val input = QrInput.Contact(firstName = " Sławek ", lastName = " Mazur ")
        val result = input.toQrContent()
        assertEquals(
            """
        BEGIN:VCARD
        VERSION:3.0
        N:Mazur;Sławek
        FN:Sławek Mazur
        END:VCARD
        
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `should map contact input with only phone`() {
        val input = QrInput.Contact(phone = " +48123456789 ")
        val result = input.toQrContent()
        assertEquals(
            """
        BEGIN:VCARD
        VERSION:3.0
        N:;
        FN:
        TEL:+48123456789
        END:VCARD

            """.trimIndent(),
            result
        )
    }

    @Test
    fun `should encode sms body with spaces and special characters`() {
        val input = QrInput.Sms(
            phoneNumber = " +48123456789 ",
            message = " QREZZY TEST? a=1&b=2 "
        )
        val result = input.toQrContent()
        assertEquals("sms:+48123456789?body=QREZZY+TEST%3F+a%3D1%26b%3D2", result)
    }

    @Test
    fun `should map email input with empty subject and body`() {
        val input = QrInput.Email(email = " contact@test.com ", subject = " ", body = " ")
        val result = input.toQrContent()
        assertEquals("MATMSG:TO:contact@test.com;SUB:;BODY:;;", result)
    }

    @Test
    fun `should return empty content when geo latitude is blank`() {
        val input = QrInput.GeoLocation(latitude = " ", longitude = "16.9252")
        val result = input.toQrContent()
        assertEquals("", result)
    }

    @Test
    fun `should return empty content when geo longitude is blank`() {
        val input = QrInput.GeoLocation(latitude = "52.4064", longitude = " ")
        val result = input.toQrContent()
        assertEquals("", result)
    }
}
