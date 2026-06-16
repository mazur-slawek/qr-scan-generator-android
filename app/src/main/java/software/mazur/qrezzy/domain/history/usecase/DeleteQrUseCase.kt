package software.mazur.qrezzy.domain.history.usecase

import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class DeleteQrUseCase @Inject constructor(private val repository: QrHistoryRepository) {
    suspend operator fun invoke(ids: List<Long>) {
        return repository.deleteByIds(ids)
    }
}