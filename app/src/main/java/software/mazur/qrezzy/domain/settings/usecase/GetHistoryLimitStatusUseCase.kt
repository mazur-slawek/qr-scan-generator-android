package software.mazur.qrezzy.domain.settings.usecase

import kotlinx.coroutines.flow.first
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import software.mazur.qrezzy.domain.settings.model.HistoryLimitStatus
import javax.inject.Inject

class GetHistoryLimitStatusUseCase @Inject constructor(
    private val qrRepository: QrRepository,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
) {
    suspend operator fun invoke(): HistoryLimitStatus {
        val settings = observeAppSettingsUseCase().first()
        val currentCount = qrRepository.getCount()

        return HistoryLimitStatus(currentCount = currentCount, historyLimit = settings.historyLimit)
    }
}