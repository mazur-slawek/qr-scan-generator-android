package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.mapper.toAppLanguage
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import java.util.Locale
import javax.inject.Inject

class InitializeAppSettingsUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(locale: Locale = Locale.getDefault()) {
        repository.initializeSettingsIfNeeded(language = locale.toAppLanguage())
    }
}