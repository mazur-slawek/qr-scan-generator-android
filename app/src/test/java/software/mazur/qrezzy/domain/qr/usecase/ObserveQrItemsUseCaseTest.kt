package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class ObserveQrItemsUseCaseTest {
    @Test
    fun `should observe qr items from repository`() = runTest {
        val items = listOf(
            createQr(id = 1L),
            createQr(id = 2L)
        )
        val repository = FakeQrRepository(initialItems = items)
        val useCase = ObserveQrItemsUseCase(repository)
        val result = useCase().first()

        assertEquals(items, result)
    }
}
