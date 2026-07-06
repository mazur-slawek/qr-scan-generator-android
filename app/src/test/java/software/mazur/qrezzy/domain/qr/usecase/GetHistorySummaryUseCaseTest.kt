package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class GetHistorySummaryUseCaseTest {
    @Test
    fun `should return history items count and latest created at`() = runTest {
        val repository = FakeQrRepository(
            initialItems = listOf(
                createQr(id = 1L, createdAt = 1_000L),
                createQr(id = 2L, createdAt = 3_000L),
                createQr(id = 3L, createdAt = 2_000L)
            )
        )
        val useCase = GetHistorySummaryUseCase(repository)
        val result = useCase()

        assertEquals(3, result.itemsCount)
        assertEquals(3_000L, result.latestCreatedAt)
    }

    @Test
    fun `should return zero count and null latest created at when history is empty`() = runTest {
        val repository = FakeQrRepository()
        val useCase = GetHistorySummaryUseCase(repository)
        val result = useCase()

        assertEquals(0, result.itemsCount)
        assertEquals(null, result.latestCreatedAt)
    }
}
