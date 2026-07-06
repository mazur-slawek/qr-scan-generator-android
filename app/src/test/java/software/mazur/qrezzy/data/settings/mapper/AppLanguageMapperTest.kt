package software.mazur.qrezzy.domain.settings.mapper

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test
import software.mazur.qrezzy.domain.settings.model.AppLanguage

class AppLanguageMapperTest {
    @Test
    fun `should map polish locale to polish app language`() {
        assertEquals(AppLanguage.POLISH, Locale.forLanguageTag("pl").toAppLanguage())
    }

    @Test
    fun `should map german locale to german app language`() {
        assertEquals(AppLanguage.GERMAN, Locale.forLanguageTag("de").toAppLanguage())
    }

    @Test
    fun `should map ukrainian locale to ukrainian app language`() {
        assertEquals(AppLanguage.UKRAINIAN, Locale.forLanguageTag("uk").toAppLanguage())
    }

    @Test
    fun `should map italian locale to italian app language`() {
        assertEquals(AppLanguage.ITALIAN, Locale.forLanguageTag("it").toAppLanguage())
    }

    @Test
    fun `should map english locale to english app language`() {
        assertEquals(AppLanguage.ENGLISH, Locale.forLanguageTag("en").toAppLanguage())
    }

    @Test
    fun `should fallback unknown locale to english app language`() {
        assertEquals(AppLanguage.ENGLISH, Locale.forLanguageTag("fr").toAppLanguage())
    }

    @Test
    fun `should map app language to language tag`() {
        assertEquals("en", AppLanguage.ENGLISH.toLanguageTag())
        assertEquals("pl", AppLanguage.POLISH.toLanguageTag())
        assertEquals("de", AppLanguage.GERMAN.toLanguageTag())
        assertEquals("uk", AppLanguage.UKRAINIAN.toLanguageTag())
        assertEquals("it", AppLanguage.ITALIAN.toLanguageTag())
    }
}
