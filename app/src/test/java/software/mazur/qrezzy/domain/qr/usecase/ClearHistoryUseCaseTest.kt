package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class ClearHistoryUseCaseTest {
    @Test
    fun `should clear all history`() = runTest {
        val repository = FakeQrRepository(
            initialItems = listOf(
                createQr(id = 1L),
                createQr(id = 2L)
            )
        )
        val useCase = ClearHistoryUseCase(repository)

        useCase()

        assertTrue(repository.deletedAll)
        assertEquals(emptyList<Any>(), repository.observeAllCurrent())
    }
}
