package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.qr.model.QrItem
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class GetQrItemByIdUseCase @Inject constructor(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long): QrItem? {
        return repository.getById(id)
    }
}