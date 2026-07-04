package software.mazur.qrezzy.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppLanguageUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppThemeUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAutoSaveScansUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetHistoryLimitUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetVibrationEnabledUseCase
import software.mazur.qrezzy.feature.settings.model.SettingsUiState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    private val setAutoSaveScansUseCase: SetAutoSaveScansUseCase,
    private val setVibrationEnabledUseCase: SetVibrationEnabledUseCase,
    private val setHistoryLimitUseCase: SetHistoryLimitUseCase,
) : ViewModel() {
    val uiState = observeAppSettingsUseCase()
        .map { settings ->
            SettingsUiState(
                language = settings.language,
                theme = settings.theme,
                autoSaveScans = settings.autoSaveScans,
                vibrationEnabled = settings.vibrationEnabled,
                historyLimit = settings.historyLimit,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)
        }
    }

    fun onThemeSelected(theme: AppTheme) {
        viewModelScope.launch {
            setAppThemeUseCase(theme)
        }
    }

    fun onAutoSaveScansChanged(value: Boolean) {
        viewModelScope.launch {
            setAutoSaveScansUseCase(value)
        }
    }

    fun onVibrationEnabledChanged(value: Boolean) {
        viewModelScope.launch {
            setVibrationEnabledUseCase(value)
        }
    }

    fun onHistoryLimitSelected(limit: HistoryLimit) {
        viewModelScope.launch {
            setHistoryLimitUseCase(limit)
        }
    }
}