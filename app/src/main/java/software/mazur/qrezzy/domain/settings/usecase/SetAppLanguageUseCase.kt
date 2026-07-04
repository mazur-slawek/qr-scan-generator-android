package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: AppLanguage) {
        repository.setLanguage(value)
    }
}