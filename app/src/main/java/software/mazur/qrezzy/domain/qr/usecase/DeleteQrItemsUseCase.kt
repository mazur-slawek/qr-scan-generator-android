package software.mazur.qrezzy.domain.qr.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class DeleteQrItemsUseCase
@Inject
constructor(private val repository: QrRepository) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteByIds(ids)
}
