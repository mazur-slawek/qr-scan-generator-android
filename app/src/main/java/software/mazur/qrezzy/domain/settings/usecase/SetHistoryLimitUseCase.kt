package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class SetHistoryLimitUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: HistoryLimit) {
        repository.setHistoryLimit(value)
    }
}