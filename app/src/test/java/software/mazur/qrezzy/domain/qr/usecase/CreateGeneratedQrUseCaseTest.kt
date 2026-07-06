package software.mazur.qrezzy.domain.qr.usecase

import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.test.FakeTimeProvider
import software.mazur.qrezzy.test.createQr

class CreateGeneratedQrUseCaseTest {
    private fun createUseCase(now: Long = NOW): CreateGeneratedQrUseCase = CreateGeneratedQrUseCase(timeProvider = FakeTimeProvider(now))

    @Test
    fun `should trim content`() {
        val useCase = createUseCase()
        val result = useCase(content = "   QREZZY TEST   ", type = QrType.TEXT, style = STYLE)
        assertEquals(
            createQr(
                id = 0L,
                content = "QREZZY TEST",
                type = QrType.TEXT,
                source = QrSource.GENERATED,
                createdAt = NOW,
                style = STYLE
            ),
            result
        )
    }

    @Test
    fun `should set source as generated`() {
        val useCase = createUseCase()
        val result = useCase(content = "TEST", type = QrType.TEXT, style = STYLE)
        assertEquals(QrSource.GENERATED, result.source)
    }

    @Test
    fun `should keep provided type`() {
        val useCase = createUseCase()
        val result = useCase(content = "https://qrezzy.app", type = QrType.URL, style = STYLE)
        assertEquals(QrType.URL, result.type)
    }

    @Test
    fun `should keep provided style`() {
        val useCase = createUseCase()
        val result = useCase(content = "TEST", type = QrType.TEXT, style = STYLE)
        assertEquals(STYLE, result.style)
    }

    @Test
    fun `should use time provider`() {
        val useCase = createUseCase(now = 987654321L)
        val result = useCase(content = "TEST", type = QrType.TEXT, style = STYLE)
        assertEquals(
            createQr(
                id = 0L,
                content = "TEST",
                type = QrType.TEXT,
                source = QrSource.GENERATED,
                createdAt = 987654321L,
                style = STYLE
            ),
            result
        )
    }

    private companion object {
        const val NOW = 123456789L
        val STYLE = QrStyle()
    }
}
