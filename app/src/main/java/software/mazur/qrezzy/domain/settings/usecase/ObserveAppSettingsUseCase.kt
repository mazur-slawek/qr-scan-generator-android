package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class ObserveAppSettingsUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    operator fun invoke() = repository.observeSettings()
}