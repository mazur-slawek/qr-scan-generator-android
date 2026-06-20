package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class DeleteQrItemsUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        suspend operator fun invoke(ids: List<Long>) = repository.deleteByIds(ids)
    }
