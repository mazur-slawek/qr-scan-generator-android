package software.mazur.qrezzy.feature.settings.model

sealed interface SettingsUiEvent {
    data object AutoSaveEnabled : SettingsUiEvent
    data object AutoSaveDisabled : SettingsUiEvent
    data object VibrationEnabled : SettingsUiEvent
    data object VibrationDisabled : SettingsUiEvent
    data object HistoryLimitChanged : SettingsUiEvent
    data object HistoryLimitExceeded : SettingsUiEvent
    data object HistoryCleared : SettingsUiEvent
}