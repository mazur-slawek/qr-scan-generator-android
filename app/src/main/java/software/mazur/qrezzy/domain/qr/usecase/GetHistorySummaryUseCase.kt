package software.mazur.qrezzy.domain.qr.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.qr.model.HistorySummary
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class GetHistorySummaryUseCase @Inject constructor(private val repository: QrRepository) {
    suspend operator fun invoke(): HistorySummary =
        HistorySummary(itemsCount = repository.getCount(), latestCreatedAt = repository.getLatestCreatedAt())
}
