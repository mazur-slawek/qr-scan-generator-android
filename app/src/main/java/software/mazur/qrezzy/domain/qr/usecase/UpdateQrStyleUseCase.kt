package software.mazur.qrezzy.domain.qr.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class UpdateQrStyleUseCase @Inject constructor(private val repository: QrRepository) {
    suspend operator fun invoke(id: Long, style: QrStyle) {
        repository.updateStyle(id = id, style = style)
    }
}
