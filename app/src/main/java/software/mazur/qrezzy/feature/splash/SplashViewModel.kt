package software.mazur.qrezzy.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.settings.usecase.InitializeAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initializeAppSettingsUseCase: InitializeAppSettingsUseCase,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase
) : ViewModel() {
    fun checkStartDestination(onOnboardingRequired: () -> Unit, onHomeRequired: () -> Unit) {
        viewModelScope.launch {
            initializeAppSettingsUseCase()
            val settings = observeAppSettingsUseCase().first()
            if (settings.onboardingCompleted) {
                onHomeRequired()
            } else {
                onOnboardingRequired()
            }
        }
    }
}
