package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: AppTheme) {
        repository.setTheme(value)
    }
}