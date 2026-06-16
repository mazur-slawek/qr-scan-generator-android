package software.mazur.qrezzy.domain.history.usecase

import software.mazur.qrezzy.core.common.time.TimeProvider
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.model.QrHistorySource
import software.mazur.qrezzy.domain.history.model.QrHistoryType
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class SaveGeneratedQrUseCase @Inject constructor(
    private val repository: QrHistoryRepository,
    private val timeProvider: TimeProvider,
) {
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
                createdAt = timeProvider.nowMillis(),
            ),
        )
    }
}