package software.mazur.qrezzy.domain.settings.usecase

import javax.inject.Inject
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository

class SetAppThemeUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: AppTheme) {
        repository.setTheme(value)
    }
}
