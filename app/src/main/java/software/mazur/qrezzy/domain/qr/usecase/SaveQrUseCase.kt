package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class SaveQrUseCase
    @Inject
    constructor(
        private val repository: QrRepository,
    ) {
        suspend operator fun invoke(qr: Qr): Long {
            require(qr.content.isNotBlank()) { "QR content cannot be empty" }
            return repository.save(qr)
        }
    }
