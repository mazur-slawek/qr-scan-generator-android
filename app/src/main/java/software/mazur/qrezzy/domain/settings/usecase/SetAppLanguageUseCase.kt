package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class SetAppLanguageUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: AppLanguage) {
        repository.setLanguage(value)
    }
}
