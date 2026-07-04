package software.mazur.qrezzy.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase
) : ViewModel() {
    fun checkStartDestination(onOnboardingRequired: () -> Unit, onHomeRequired: () -> Unit) {
        viewModelScope.launch {
            val settings = observeAppSettingsUseCase().first()
            if (settings.onboardingCompleted) {
                onHomeRequired()
            } else {
                onOnboardingRequired()
            }
        }
    }
}