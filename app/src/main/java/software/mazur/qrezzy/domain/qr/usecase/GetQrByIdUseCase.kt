package software.mazur.qrezzy.domain.qr.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class GetQrByIdUseCase
@Inject
constructor(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long): Qr? = repository.getById(id)
}
