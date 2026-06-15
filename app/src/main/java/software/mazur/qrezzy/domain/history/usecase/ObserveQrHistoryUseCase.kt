package software.mazur.qrezzy.domain.history.usecase

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class ObserveQrHistoryUseCase @Inject constructor(
    private val repository: QrHistoryRepository,
) {
    operator fun invoke(): Flow<List<QrHistoryItem>> {
        return repository.observeAll()
    }
}