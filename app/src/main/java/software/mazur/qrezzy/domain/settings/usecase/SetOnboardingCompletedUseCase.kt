package software.mazur.qrezzy.domain.settings.usecase

import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class SetOnboardingCompletedUseCase @Inject constructor(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(value: Boolean) {
        repository.setOnboardingCompleted(value)
    }
}