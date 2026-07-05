package software.mazur.qrezzy.domain.qr.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class ObserveQrItemsUseCase @Inject constructor(private val repository: QrRepository) {
    operator fun invoke(): Flow<List<Qr>> = repository.observeAll()
}
