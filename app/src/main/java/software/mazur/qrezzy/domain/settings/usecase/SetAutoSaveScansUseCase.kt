package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class SetAutoSaveScansUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: Boolean) {
        repository.setAutoSaveScans(value)
    }
}
