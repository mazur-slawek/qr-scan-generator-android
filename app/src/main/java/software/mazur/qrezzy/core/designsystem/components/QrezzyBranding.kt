package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import software.mazur.qrezzy.R
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun QrezzyBranding() {
    val visibleLetterCount = remember { mutableIntStateOf(QrezzyBrandingConfig.INITIAL_VISIBLE_LETTER_COUNT) }
    val showSlogan = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(QrezzyBrandingConfig.INITIAL_ANIMATION_DELAY_MS.milliseconds)

        for (count in QrezzyBrandingConfig.FIRST_ANIMATED_LETTER_INDEX..QrezzyBrandingConfig.TOTAL_LETTER_COUNT) {
            visibleLetterCount.intValue = count
            delay(QrezzyBrandingConfig.LETTER_APPEAR_DELAY_MS.milliseconds)
        }

        delay(QrezzyBrandingConfig.SLOGAN_APPEAR_DELAY_MS.milliseconds)
        showSlogan.value = true
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
    ) {
        val letterWidth =
            calculateLetterWidth(
                availableWidth = maxWidth,
            )
        val sloganWidth = maxWidth * QrezzyBrandingConfig.SLOGAN_WIDTH_FACTOR

        Column(
            modifier = Modifier.width(maxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QrezzyLogoLetter(
                    resId = R.drawable.logo_q,
                    width = letterWidth,
                    alpha = QrezzyBrandingConfig.VISIBLE_ALPHA,
                )

                QrezzyLogoLetter(
                    resId = R.drawable.logo_r,
                    width = letterWidth,
                    alpha = QrezzyBrandingConfig.VISIBLE_ALPHA,
                )

                QrezzyLogoLetter(
                    resId = R.drawable.logo_e,
                    width = letterWidth,
                    alpha =
                        calculateLetterAlpha(
                            visibleLetterCount = visibleLetterCount.intValue,
                            requiredLetterCount = QrezzyBrandingConfig.LOGO_E_VISIBLE_COUNT,
                        ),
                )

                QrezzyLogoLetter(
                    resId = R.drawable.logo_z_1,
                    width = letterWidth,
                    alpha =
                        calculateLetterAlpha(
                            visibleLetterCount = visibleLetterCount.intValue,
                            requiredLetterCount = QrezzyBrandingConfig.LOGO_FIRST_Z_VISIBLE_COUNT,
                        ),
                )

                QrezzyLogoLetter(
                    resId = R.drawable.logo_z_2,
                    width = letterWidth,
                    alpha =
                        calculateLetterAlpha(
                            visibleLetterCount = visibleLetterCount.intValue,
                            requiredLetterCount = QrezzyBrandingConfig.LOGO_SECOND_Z_VISIBLE_COUNT,
                        ),
                )

                QrezzyLogoLetter(
                    resId = R.drawable.logo_y,
                    width = letterWidth,
                    alpha =
                        calculateLetterAlpha(
                            visibleLetterCount = visibleLetterCount.intValue,
                            requiredLetterCount = QrezzyBrandingConfig.LOGO_Y_VISIBLE_COUNT,
                        ),
                )
            }

            Spacer(
                modifier = Modifier.height(QrezzyBrandingConfig.SLOGAN_TOP_SPACING),
            )

            QrezzySlogan(
                width = sloganWidth,
                isVisible = showSlogan.value,
            )
        }
    }
}

@Composable
private fun QrezzyLogoLetter(
    resId: Int,
    width: Dp,
    alpha: Float,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec =
            tween(
                durationMillis = QrezzyBrandingConfig.LETTER_FADE_DURATION_MS,
            ),
        label = "qrezzy_logo_letter_alpha",
    )

    Image(
        modifier =
            Modifier
                .width(width)
                .alpha(animatedAlpha),
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}

@Composable
private fun QrezzySlogan(
    width: Dp,
    isVisible: Boolean,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue =
            if (isVisible) {
                QrezzyBrandingConfig.VISIBLE_ALPHA
            } else {
                QrezzyBrandingConfig.INVISIBLE_ALPHA
            },
        animationSpec =
            tween(
                durationMillis = QrezzyBrandingConfig.SLOGAN_FADE_DURATION_MS,
            ),
        label = "qrezzy_slogan_alpha",
    )

    Image(
        modifier =
            Modifier
                .width(width)
                .alpha(animatedAlpha)
                .padding(start = 10.dp),
        painter = painterResource(id = R.drawable.slogan_scan_create_share),
        contentDescription = QrezzyBrandingConfig.SLOGAN_CONTENT_DESCRIPTION,
        contentScale = ContentScale.Fit,
    )
}

/**
 * Oblicza szerokość pojedynczej litery logo na podstawie dostępnej szerokości.
 *
 * Dzięki ograniczeniu maksymalnej szerokości logo nie staje się
 * zbyt duże na większych ekranach.
 */
private fun calculateLetterWidth(availableWidth: Dp): Dp =
    (availableWidth / QrezzyBrandingConfig.TOTAL_LETTER_COUNT)
        .coerceAtMost(QrezzyBrandingConfig.MAX_LETTER_WIDTH)

/**
 * Zwraca docelową przezroczystość litery logo.
 */
private fun calculateLetterAlpha(
    visibleLetterCount: Int,
    requiredLetterCount: Int,
): Float =
    if (visibleLetterCount >= requiredLetterCount) {
        QrezzyBrandingConfig.VISIBLE_ALPHA
    } else {
        QrezzyBrandingConfig.INVISIBLE_ALPHA
    }

/**
 * Stałe konfiguracyjne animowanego brandingu QREZZY.
 */
private object QrezzyBrandingConfig {
    const val TOTAL_LETTER_COUNT = 6
    const val INITIAL_VISIBLE_LETTER_COUNT = 2
    const val FIRST_ANIMATED_LETTER_INDEX = 3
    const val LOGO_E_VISIBLE_COUNT = 3
    const val LOGO_FIRST_Z_VISIBLE_COUNT = 4
    const val LOGO_SECOND_Z_VISIBLE_COUNT = 5
    const val LOGO_Y_VISIBLE_COUNT = 6
    const val INITIAL_ANIMATION_DELAY_MS = 250
    const val LETTER_APPEAR_DELAY_MS = 180
    const val SLOGAN_APPEAR_DELAY_MS = 100
    const val LETTER_FADE_DURATION_MS = 350
    const val SLOGAN_FADE_DURATION_MS = 600
    const val VISIBLE_ALPHA = 1f
    const val INVISIBLE_ALPHA = 0f
    const val SLOGAN_WIDTH_FACTOR = 0.65f
    val MAX_LETTER_WIDTH = 60.dp
    val SLOGAN_TOP_SPACING = 12.dp
    const val SLOGAN_CONTENT_DESCRIPTION = "Scan. Create. Share."
}
