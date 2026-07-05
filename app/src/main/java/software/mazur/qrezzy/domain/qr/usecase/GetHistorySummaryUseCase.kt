package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.domain.qr.model.HistorySummary
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class GetHistorySummaryUseCase @Inject constructor(private val repository: QrRepository) {
    suspend operator fun invoke(): HistorySummary {
        return HistorySummary(itemsCount = repository.getCount(), latestCreatedAt = repository.getLatestCreatedAt())
    }
}