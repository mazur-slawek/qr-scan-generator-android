package software.mazur.qrezzy.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import software.mazur.qrezzy.domain.settings.model.AppTheme

private val LightColorScheme = lightColorScheme(
    primary = QrezzyMintDark,
    secondary = QrezzyPurpleDark,
    tertiary = QrezzyPinkDark,
    background = Surface,
    surface = Surface,
    error = Error,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = TextPrimary,
)
private val DarkColorScheme = darkColorScheme(
    primary = QrezzyMint,
    secondary = QrezzyPurple,
    tertiary = QrezzyPink,
    background = DarkSurface,
    surface = DarkSurface,
    error = Error,
    onPrimary = DarkTextPrimary,
    onSecondary = DarkTextPrimary,
    onTertiary = DarkTextPrimary,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onError = DarkTextPrimary,
)

@Composable
fun QREZZYTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val useDarkTheme =
        when (appTheme) {
            AppTheme.SYSTEM -> systemDark
            AppTheme.LIGHT  -> false
            AppTheme.DARK   -> true
        }
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as Activity
            activity.window.statusBarColor = colorScheme.background.toArgb()
            activity.window.navigationBarColor = colorScheme.background.toArgb()

            WindowCompat.getInsetsController(activity.window, view).apply {
                isAppearanceLightStatusBars = !useDarkTheme
                isAppearanceLightNavigationBars = !useDarkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}