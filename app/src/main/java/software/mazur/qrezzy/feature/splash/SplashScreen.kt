package software.mazur.qrezzy.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedBackground
import software.mazur.qrezzy.core.designsystem.components.QrezzyBranding

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        QrezzyAnimatedBackground()
        QrezzyBranding(
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
