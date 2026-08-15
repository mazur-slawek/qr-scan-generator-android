package software.mazur.qrezzy.core.qr.decoder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QrImageDecoderTest {
    @Test
    fun `should not downsample image already within max dimension`() {
        assertEquals(1, QrImageDecoder.calculateInSampleSize(width = 800, height = 600, maxDimension = 1600))
    }

    @Test
    fun `should not downsample image exactly at max dimension`() {
        assertEquals(1, QrImageDecoder.calculateInSampleSize(width = 1600, height = 900, maxDimension = 1600))
    }

    @Test
    fun `should never upscale a small image`() {
        assertEquals(1, QrImageDecoder.calculateInSampleSize(width = 100, height = 100, maxDimension = 1600))
    }

    @Test
    fun `should downsample large landscape image`() {
        val inSampleSize = QrImageDecoder.calculateInSampleSize(width = 4000, height = 3000, maxDimension = 1600)

        assertEquals(4, inSampleSize)
    }

    @Test
    fun `should downsample large portrait image symmetrically`() {
        val inSampleSize = QrImageDecoder.calculateInSampleSize(width = 3000, height = 4000, maxDimension = 1600)

        assertEquals(4, inSampleSize)
    }

    @Test
    fun `should downsample very large image more aggressively`() {
        val inSampleSize = QrImageDecoder.calculateInSampleSize(width = 8000, height = 6000, maxDimension = 1600)

        assertEquals(8, inSampleSize)
    }

    @Test
    fun `should keep sampled dimensions within max dimension for large images`() {
        val width = 4000
        val height = 3000
        val maxDimension = 1600

        val inSampleSize = QrImageDecoder.calculateInSampleSize(width, height, maxDimension)

        assertTrue(width / inSampleSize <= maxDimension)
        assertTrue(height / inSampleSize <= maxDimension)
    }

    @Test
    fun `should only use power of two sample sizes`() {
        val inSampleSize = QrImageDecoder.calculateInSampleSize(width = 5000, height = 5000, maxDimension = 1600)

        assertEquals(0, inSampleSize and (inSampleSize - 1))
    }
}
