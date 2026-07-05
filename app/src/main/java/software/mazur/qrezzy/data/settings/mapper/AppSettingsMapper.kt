package software.mazur.qrezzy.data.settings.mapper

import software.mazur.qrezzy.data.settings.local.AppSettingsEntity
import software.mazur.qrezzy.domain.settings.model.AppSettings

fun AppSettingsEntity.toDomain(): AppSettings = AppSettings(
    onboardingCompleted = onboardingCompleted,
    language = language,
    theme = theme,
    autoSaveScans = autoSaveScans,
    vibrationEnabled = vibrationEnabled,
    historyLimit = historyLimit
)
