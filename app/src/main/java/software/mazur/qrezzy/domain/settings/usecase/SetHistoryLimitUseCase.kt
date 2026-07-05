package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class SetHistoryLimitUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: HistoryLimit) {
        repository.setHistoryLimit(value)
    }
}
