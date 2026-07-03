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
import androidx.compose.runtime.setValue
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
    var isButtonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(SplashScreenDefaults.Animation.BUTTON_APPEAR_DELAY_MILLIS.milliseconds)
        isButtonVisible = true
    }
    val buttonAlpha by animateFloatAsState(
        targetValue =
            if (isButtonVisible) {
                SplashScreenDefaults.Animation.VISIBLE_ALPHA
            } else {
                SplashScreenDefaults.Animation.HIDDEN_ALPHA
            },
        animationSpec = tween(durationMillis = SplashScreenDefaults.Animation.BUTTON_ANIMATION_DURATION_MILLIS),
        label = SplashScreenDefaults.Animation.BUTTON_ALPHA_LEVEL,
    )
    val buttonOffsetY by animateDpAsState(
        targetValue =
            if (isButtonVisible) {
                SplashScreenDefaults.Animation.finalButtonOffsetY
            } else {
                SplashScreenDefaults.Animation.initialButtonOffsetY
            },
        animationSpec = tween(durationMillis = SplashScreenDefaults.Animation.BUTTON_ANIMATION_DURATION_MILLIS),
        label = SplashScreenDefaults.Animation.BUTTON_OFFSET_Y_LABEL,
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        QrezzyAnimatedBackground()

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SplashScreenDefaults.Layout.contentHorizontalPadding),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(SplashScreenDefaults.Layout.BRANDING_WEIGHT),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qrezzy_mascot_launch),
                    contentDescription = SplashScreenDefaults.LOGO_CONTENT_DESCRIPTION,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(SplashScreenDefaults.Layout.brandingSpacing))

                QrezzyBranding()
            }

            QrezzyButton(
                text = stringResource(id = R.string.button_get_started),
                onClick = onGetStartedClick,
                rightIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                modifier =
                    Modifier
                        .alpha(buttonAlpha)
                        .offset(y = buttonOffsetY)
                        .padding(bottom = SplashScreenDefaults.Layout.buttonBottomPadding),
            )
        }
    }
}

private object SplashScreenDefaults {
    const val LOGO_CONTENT_DESCRIPTION = "QREZZY logo"

    object Layout {
        const val BRANDING_WEIGHT = 1f
        val contentHorizontalPadding = 16.dp
        val brandingSpacing = 32.dp
        val buttonBottomPadding = 70.dp
    }

    object Animation {
        const val BUTTON_APPEAR_DELAY_MILLIS = 1_100
        const val BUTTON_ANIMATION_DURATION_MILLIS = 700
        const val VISIBLE_ALPHA = 1f
        const val HIDDEN_ALPHA = 0f
        val initialButtonOffsetY = 32.dp
        val finalButtonOffsetY = 0.dp
        const val BUTTON_ALPHA_LEVEL = "splash_button_alpha"
        const val BUTTON_OFFSET_Y_LABEL = "splash_button_offset_y"
    }
}
