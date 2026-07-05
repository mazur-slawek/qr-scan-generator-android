package software.mazur.qrezzy.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    // Main app background and reusable surfaces.
    background = Color(0xFFF9F9F9),
    surface = Color(0xFFFFFFFF),
    // Borders used for cards, inputs, tabs and outlined components.
    surfaceContainer = Color(0xFFE5E5E5),
    surfaceDim = Color(0xFF000000),
    // Text colors used for titles, descriptions and disabled states.
    onSurface = Color(0xFF222222),
    onSurfaceVariant = Color(0xFF4D4D4D),
    inverseOnSurface = Color(0xFF222222),
    surfaceTint = Color(0xFFA0A0A0)
)
private val DarkColorScheme = darkColorScheme(
    // Main app background and reusable surfaces.
    background = Color(0xFF15161F),
    surface = Color(0xFF1E2030),
    // Borders used for cards, inputs, tabs and outlined components.
    surfaceContainer = Color(0xFF3D4167),
    surfaceDim = Color(0xFF434664),
    // Text colors used for titles, descriptions and disabled states.
    onSurface = Color(0xFFF7F4FF),
    onSurfaceVariant = Color(0xFFCECECE),
    inverseOnSurface = Color(0xFF222222),
    surfaceTint = Color(0xFFA0A0A0)
)

@Composable
fun QREZZYTheme(isDarkTheme: Boolean, content: @Composable () -> Unit) {
    val view = LocalView.current
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography) {
        CompositionLocalProvider(LocalIsDarkTheme provides isDarkTheme) {
            content()
        }
    }
}
