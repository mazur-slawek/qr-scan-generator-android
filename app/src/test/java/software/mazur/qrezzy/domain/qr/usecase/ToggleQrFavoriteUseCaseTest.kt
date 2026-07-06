package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.test.FakeQrRepository
import software.mazur.qrezzy.test.createQr

class ToggleQrFavoriteUseCaseTest {
    @Test
    fun `should update favorite status`() = runTest {
        val repository = FakeQrRepository(
            initialItems = listOf(createQr(id = 1L, isFavorite = false))
        )
        val useCase = ToggleQrFavoriteUseCase(repository)

        useCase(id = 1L, isFavorite = true)

        assertEquals(1L to true, repository.updatedFavorite)
        assertEquals(
            createQr(id = 1L, isFavorite = true),
            repository.observeAllCurrent().first()
        )
    }
}
