package software.mazur.qrezzy.feature.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedBackground
import software.mazur.qrezzy.core.designsystem.components.QrezzyBranding
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(onGetStartedClick: () -> Unit) {
    val isButtonVisible = remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        delay(SplashScreenDefaults.BUTTON_APPEAR_DELAY_MS.milliseconds)
        isButtonVisible.value = true
    }
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isButtonVisible.value) {
            SplashScreenDefaults.VISIBLE_ALPHA
        } else {
            SplashScreenDefaults.HIDDEN_ALPHA
        },
        animationSpec = tween(
            durationMillis = SplashScreenDefaults.BUTTON_ANIMATION_DURATION_MS,
        ),
        label = SplashScreenAnimationLabels.BUTTON_ALPHA,
    )
    val buttonOffsetY by animateDpAsState(
        targetValue = if (isButtonVisible.value) {
            0.dp
        } else {
            SplashScreenDefaults.BUTTON_INITIAL_OFFSET_Y
        },
        animationSpec = tween(
            durationMillis = SplashScreenDefaults.BUTTON_ANIMATION_DURATION_MS,
        ),
        label = SplashScreenAnimationLabels.BUTTON_OFFSET_Y,
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        QrezzyAnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SplashScreenDefaults.CONTENT_HORIZONTAL_PADDING,
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.qrezzy_logo),
                    contentDescription = SplashScreenDefaults.LOGO_CONTENT_DESCRIPTION,
                )
                Spacer(modifier = Modifier.height(SplashScreenDefaults.BRANDING_SPACING))
                QrezzyBranding()
            }

            QrezzyButton(
                onClick = onGetStartedClick,
                text = stringResource(id = R.string.button_get_started),
                rightIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                modifier = Modifier
                    .alpha(buttonAlpha)
                    .padding(bottom = SplashScreenDefaults.BUTTON_BOTTOM_PADDING)
                    .offset(y = buttonOffsetY),
            )
        }
    }
}

/**
 * Stałe konfiguracyjne ekranu Splash.
 */
private object SplashScreenDefaults {
    const val BUTTON_APPEAR_DELAY_MS = 1_100
    const val BUTTON_ANIMATION_DURATION_MS = 700
    const val VISIBLE_ALPHA = 1f
    const val HIDDEN_ALPHA = 0f
    val CONTENT_HORIZONTAL_PADDING = 32.dp
    val BRANDING_SPACING = 32.dp
    val BUTTON_BOTTOM_PADDING = 70.dp
    val BUTTON_INITIAL_OFFSET_Y = 32.dp
    const val LOGO_CONTENT_DESCRIPTION = "QREZZY logo"
}

/**
 * Identyfikatory animacji używane przez Compose Animation Inspector.
 */
private object SplashScreenAnimationLabels {
    const val BUTTON_ALPHA = "splash_button_alpha"
    const val BUTTON_OFFSET_Y = "splash_button_offset_y"
}