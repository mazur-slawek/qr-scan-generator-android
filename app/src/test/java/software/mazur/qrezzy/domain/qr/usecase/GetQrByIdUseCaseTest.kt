package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class GetQrByIdUseCaseTest {
    @Test
    fun `should return qr by id`() = runTest {
        val expectedQr = createQr(id = 2L, content = "Expected")
        val repository = FakeQrRepository(
            initialItems = listOf(
                createQr(id = 1L),
                expectedQr
            )
        )
        val useCase = GetQrByIdUseCase(repository)
        val result = useCase(2L)

        assertEquals(expectedQr, result)
    }

    @Test
    fun `should return null when qr does not exist`() = runTest {
        val repository = FakeQrRepository(
            initialItems = listOf(createQr(id = 1L))
        )
        val useCase = GetQrByIdUseCase(repository)
        val result = useCase(999L)

        assertNull(result)
    }
}
