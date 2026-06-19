package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.core.common.time.TimeProvider
import software.mazur.qrezzy.domain.qr.model.QrItem
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class SaveGeneratedQrItemUseCase @Inject constructor(
    private val repository: QrRepository,
    private val timeProvider: TimeProvider,
) {
    suspend operator fun invoke(type: QrType, title: String, content: String, payloadJson: String?): Long {
        return repository.save(
            QrItem(
                source = QrSource.GENERATED,
                type = type,
                title = title,
                content = content,
                payloadJson = payloadJson,
                createdAt = timeProvider.nowMillis(),
            )
        )
    }
}