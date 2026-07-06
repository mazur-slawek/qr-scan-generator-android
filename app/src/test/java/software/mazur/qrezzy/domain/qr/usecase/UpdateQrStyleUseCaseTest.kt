package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class UpdateQrStyleUseCaseTest {
    @Test
    fun `should update qr style`() = runTest {
        val newStyle = QrStyle(
            qrColor = 0xFF000000,
            backgroundColor = 0xFFFFFFFF,
            patternStyle = QrPatternStyle.ROUNDED,
            errorCorrection = QrErrorCorrection.HIGH
        )
        val repository = FakeQrRepository(
            initialItems = listOf(
                createQr(id = 1L)
            )
        )
        val useCase = UpdateQrStyleUseCase(repository)

        useCase(id = 1L, style = newStyle)

        assertEquals(1L to newStyle, repository.updatedStyle)
        assertEquals(
            newStyle,
            repository.observeAllCurrent().first().style
        )
    }
}
