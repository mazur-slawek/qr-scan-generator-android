package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class DeleteQrItemsUseCaseTest {
    @Test
    fun `should delete selected qr items`() = runTest {
        val repository = FakeQrRepository(
            initialItems = listOf(
                createQr(id = 1L),
                createQr(id = 2L),
                createQr(id = 3L)
            )
        )
        val useCase = DeleteQrItemsUseCase(repository)

        useCase(listOf(1L, 3L))

        assertEquals(listOf(1L, 3L), repository.deletedIds)
        assertEquals(listOf(createQr(id = 2L)), repository.observeAllCurrent())
    }
}
