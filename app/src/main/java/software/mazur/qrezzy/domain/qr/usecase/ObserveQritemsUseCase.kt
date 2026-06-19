package software.mazur.qrezzy.domain.qr.usecase

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.qr.model.QrItem
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class ObserveQritemsUseCase @Inject constructor(private val repository: QrRepository) {
    operator fun invoke(): Flow<List<QrItem>> {
        return repository.observeAll()
    }
}