package software.mazur.qrezzy.domain.history.usecase

import software.mazur.qrezzy.data.history.local.QrHistorySource
import software.mazur.qrezzy.data.history.local.QrHistoryType
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class SaveGeneratedQrUseCase @Inject constructor(private val repository: QrHistoryRepository) {
    suspend operator fun invoke(
        type: QrHistoryType,
        title: String,
        content: String,
        payloadJson: String?,
    ): Long {
        return repository.save(
            QrHistoryItem(
                source = QrHistorySource.GENERATED,
                type = type,
                title = title,
                content = content,
                payloadJson = payloadJson,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}