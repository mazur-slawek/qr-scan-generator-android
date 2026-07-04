package software.mazur.qrezzy.domain.settings.mapper

import software.mazur.qrezzy.domain.settings.model.AppLanguage
import java.util.Locale

fun Locale.toAppLanguage(): AppLanguage {
    return when (language.lowercase()) {
        "pl" -> AppLanguage.POLISH
        "de" -> AppLanguage.GERMAN
        "uk" -> AppLanguage.UKRAINIAN
        "it" -> AppLanguage.ITALIAN
        "en" -> AppLanguage.ENGLISH
        else -> AppLanguage.ENGLISH
    }
}