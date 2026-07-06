package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class SaveQrUseCaseTest {
    @Test
    fun `should save valid qr`() = runTest {
        val repository = FakeQrRepository()
        val useCase = SaveQrUseCase(repository)
        val qr = createQr(id = 0L, content = "QREZZY")
        useCase(qr)
        assertEquals(listOf(qr.copy(id = 1L)), repository.savedItems)
    }

    @Test
    fun `should throw IllegalArgumentException for blank content`() = kotlinx.coroutines.test.runTest {
        val repository = FakeQrRepository()
        val useCase = SaveQrUseCase(repository)
        val qr = createQr(content = "   ")

        assertThrows(IllegalArgumentException::class.java) {
            kotlinx.coroutines.test.runTest {
                useCase(qr)
            }
        }

        assertEquals(emptyList<Qr>(), repository.savedItems)
    }
}
