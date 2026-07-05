package software.mazur.qrezzy.domain.settings.mapper

import java.util.Locale
import software.mazur.qrezzy.domain.settings.model.AppLanguage

fun Locale.toAppLanguage(): AppLanguage = when (language.lowercase()) {
    "pl" -> AppLanguage.POLISH
    "de" -> AppLanguage.GERMAN
    "uk" -> AppLanguage.UKRAINIAN
    "it" -> AppLanguage.ITALIAN
    "en" -> AppLanguage.ENGLISH
    else -> AppLanguage.ENGLISH
}

fun AppLanguage.toLanguageTag(): String = when (this) {
    AppLanguage.ENGLISH -> "en"
    AppLanguage.POLISH -> "pl"
    AppLanguage.GERMAN -> "de"
    AppLanguage.UKRAINIAN -> "uk"
    AppLanguage.ITALIAN -> "it"
}
