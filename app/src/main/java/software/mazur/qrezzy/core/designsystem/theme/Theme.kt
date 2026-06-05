package software.mazur.qrezzy.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        primary = QrezzyMint,
        secondary = QrezzyYellow,
        tertiary = QrezzyPink,
        background = Background,
        surface = Surface,
    )

@Composable
fun QREZZYTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
// TODO: Add dark theme support when the light design system is completed.
//    val colorScheme = when {
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
