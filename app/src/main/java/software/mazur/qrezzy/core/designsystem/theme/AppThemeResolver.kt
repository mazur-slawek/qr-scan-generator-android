package software.mazur.qrezzy.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import software.mazur.qrezzy.domain.settings.model.AppTheme

@Composable
fun resolveIsDarkTheme(appTheme: AppTheme): Boolean {
    val systemDark = isSystemInDarkTheme()
    return when (appTheme) {
        AppTheme.SYSTEM -> systemDark
        AppTheme.LIGHT  -> false
        AppTheme.DARK   -> true
    }
}