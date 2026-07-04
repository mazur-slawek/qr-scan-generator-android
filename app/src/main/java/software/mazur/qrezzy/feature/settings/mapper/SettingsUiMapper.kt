package software.mazur.qrezzy.feature.settings.mapper

import software.mazur.qrezzy.domain.settings.model.AppLanguage
import software.mazur.qrezzy.domain.settings.model.AppTheme
import software.mazur.qrezzy.domain.settings.model.HistoryLimit

fun AppLanguage.displayName(): String {
    return when (this) {
        AppLanguage.SYSTEM    -> "System"
        AppLanguage.ENGLISH   -> "English"
        AppLanguage.POLISH    -> "Polski"
        AppLanguage.GERMAN    -> "Deutsch"
        AppLanguage.UKRAINIAN -> "Українська"
        AppLanguage.ITALIAN   -> "Italiano"
    }
}

fun AppTheme.displayName(): String {
    return when (this) {
        AppTheme.SYSTEM -> "System"
        AppTheme.LIGHT  -> "Light"
        AppTheme.DARK   -> "Dark"
    }
}

fun HistoryLimit.displayName(): String {
    return when (this) {
        HistoryLimit.ITEMS_100  -> "100"
        HistoryLimit.ITEMS_200  -> "200"
        HistoryLimit.ITEMS_500  -> "500"
        HistoryLimit.ITEMS_1000 -> "1000"
        HistoryLimit.UNLIMITED  -> "Unlimited"
    }
}