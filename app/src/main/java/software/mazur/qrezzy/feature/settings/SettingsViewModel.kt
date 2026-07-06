package software.mazur.qrezzy.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.common.crash.CrashReporter
import software.mazur.qrezzy.core.common.vibration.VibrationService
import software.mazur.qrezzy.core.localization.QrezzyLocaleManager
import software.mazur.qrezzy.domain.qr.model.HistorySummary
import software.mazur.qrezzy.domain.qr.usecase.ClearHistoryUseCase
import software.mazur.qrezzy.domain.qr.usecase.GetHistorySummaryUseCase
import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit
import software.mazur.qrezzy.domain.settings.usecase.GetHistoryLimitStatusUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppLanguageUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAppThemeUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetAutoSaveScansUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetHistoryLimitUseCase
import software.mazur.qrezzy.domain.settings.usecase.SetVibrationEnabledUseCase
import software.mazur.qrezzy.feature.settings.model.SettingsUiEvent
import software.mazur.qrezzy.feature.settings.model.SettingsUiState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val crashReporter: CrashReporter,
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    private val setAutoSaveScansUseCase: SetAutoSaveScansUseCase,
    private val setVibrationEnabledUseCase: SetVibrationEnabledUseCase,
    private val setHistoryLimitUseCase: SetHistoryLimitUseCase,
    private val getHistoryLimitStatusUseCase: GetHistoryLimitStatusUseCase,
    private val getHistorySummaryUseCase: GetHistorySummaryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val localeManager: QrezzyLocaleManager,
    private val vibrationService: VibrationService
) : ViewModel() {
    private val historySummary =
        MutableStateFlow(HistorySummary(itemsCount = 0, latestCreatedAt = null))
    private val showClearHistoryDialog = MutableStateFlow(false)
    private val showHistoryLimitReachedPopup = MutableStateFlow(false)
    private val _events = MutableSharedFlow<SettingsUiEvent>()
    val events = _events.asSharedFlow()
    val uiState =
        combine(
            observeAppSettingsUseCase(),
            historySummary,
            showClearHistoryDialog,
            showHistoryLimitReachedPopup
        ) { settings, summary, showClearDialog, showLimitReachedPopup ->
            SettingsUiState(
                isLoading = false,
                theme = settings.theme,
                language = settings.language,
                historyLimit = settings.historyLimit,
                autoSaveScans = settings.autoSaveScans,
                vibrationEnabled = settings.vibrationEnabled,
                historyItemsCount = summary.itemsCount,
                latestHistoryItemCreatedAt = summary.latestCreatedAt,
                showClearHistoryDialog = showClearDialog,
                showHistoryLimitReachedPopup = showLimitReachedPopup
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = SettingsUiState(isLoading = true),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)
            localeManager.applyLanguage(language)
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

            _events.emit(
                if (value) {
                    SettingsUiEvent.AutoSaveEnabled
                } else {
                    SettingsUiEvent.AutoSaveDisabled
                }
            )
        }
    }

    fun onVibrationEnabledChanged(value: Boolean) {
        viewModelScope.launch {
            setVibrationEnabledUseCase(value)

            if (value) {
                vibrationService.vibrateShort()
            }

            _events.emit(
                if (value) {
                    SettingsUiEvent.VibrationEnabled
                } else {
                    SettingsUiEvent.VibrationDisabled
                }
            )
        }
    }

    fun onHistoryLimitSelected(limit: HistoryLimit) {
        crashReporter.log("Settings: history limit selected")
        viewModelScope.launch {
            setHistoryLimitUseCase(limit)
            refreshHistorySummary()
            refreshHistoryLimitState()

            if (showHistoryLimitReachedPopup.value) {
                crashReporter.log("Settings: selected history limit already reached")
            }
            _events.emit(SettingsUiEvent.HistoryLimitChanged)
        }
    }

    suspend fun refreshHistoryLimitState() {
        val status = getHistoryLimitStatusUseCase()
        showHistoryLimitReachedPopup.value = status.isLimitReached
    }

    fun refreshHistorySummary() {
        viewModelScope.launch {
            historySummary.value = getHistorySummaryUseCase()
        }
    }

    fun onClearHistoryClick() {
        crashReporter.log("Settings: clear history dialog opened")
        showClearHistoryDialog.value = true
    }

    fun onClearHistoryDialogDismissed() {
        showClearHistoryDialog.value = false
    }

    fun onClearHistoryConfirmed() {
        crashReporter.log("Settings: clear history confirmed")
        viewModelScope.launch {
            runCatching {
                clearHistoryUseCase()
            }.onSuccess {
                crashReporter.log("Settings: history cleared successfully")
                showClearHistoryDialog.value = false
                refreshHistorySummary()
                refreshHistoryLimitState()
                _events.emit(SettingsUiEvent.HistoryCleared)
            }.onFailure { error ->
                crashReporter.log("Settings: clear history failed")
                crashReporter.recordException(error)
                showClearHistoryDialog.value = false
            }
        }
    }
}
