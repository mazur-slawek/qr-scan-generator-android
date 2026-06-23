package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class ToggleQrFavoriteUseCase @Inject constructor(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long, isFavorite: Boolean) {
        repository.updateFavoriteStatus(id = id, isFavorite = isFavorite)
    }
}