package software.mazur.qrezzy.core.qr.renderer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

class QrBitmapGeneratorTest {
    private val generator = QrBitmapGenerator()

    @Ignore("Requires Android Bitmap. Should be covered after extracting a Bitmap seam or by Robolectric tests.")
    @Test
    fun `should return success with bitmap for valid content`() {
        val result = generator.generate(content = "https://qrezzy.app")

        assertTrue(result is QrGenerationResult.Success)
    }

    @Test
    fun `should return null for blank content`() {
        val result = generator.generate(content = "   ")

        assertNull(result)
    }

    @Test
    fun `should return CannotEncode when content exceeds capacity for selected error correction level`() {
        val tooLongContent = "A".repeat(2000)

        val result = generator.generate(
            content = tooLongContent,
            style = QrStyle(errorCorrection = QrErrorCorrection.HIGH)
        )

        assertEquals(QrGenerationResult.CannotEncode, result)
    }

    @Test
    fun `should not throw when content exceeds capacity`() {
        val tooLongContent = "A".repeat(2000)

        generator.generate(content = tooLongContent, style = QrStyle(errorCorrection = QrErrorCorrection.HIGH))
    }
}
