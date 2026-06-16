package software.mazur.qrezzy.domain.history.usecase

import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class GetQrByIdUseCase @Inject constructor(private val repository: QrHistoryRepository) {
    suspend operator fun invoke(id: Long): QrHistoryItem? {
        return repository.getById(id)
    }
}