package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class ObserveAppSettingsUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    operator fun invoke() = repository.observeSettings()
}
