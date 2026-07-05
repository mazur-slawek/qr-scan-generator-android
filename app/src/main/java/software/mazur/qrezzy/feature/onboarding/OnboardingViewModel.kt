package software.mazur.qrezzy.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.settings.usecase.SetOnboardingCompletedUseCase
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {
    fun completeOnboarding(onCompleted: () -> Unit) {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
            onCompleted()
        }
    }
}