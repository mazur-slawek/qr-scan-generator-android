package software.mazur.qrezzy.core.localization

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject
import javax.inject.Singleton
import software.mazur.qrezzy.domain.settings.mapper.toLanguageTag
import software.mazur.qrezzy.domain.settings.model.AppLanguage

@Singleton
class QrezzyLocaleManager @Inject constructor() {
    fun applyLanguage(language: AppLanguage) {
        val locales = LocaleListCompat.forLanguageTags(language.toLanguageTag())
        if (AppCompatDelegate.getApplicationLocales() != locales) {
            AppCompatDelegate.setApplicationLocales(locales)
        }
    }
}
