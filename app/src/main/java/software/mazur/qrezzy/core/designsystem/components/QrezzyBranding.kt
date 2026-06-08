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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun QrezzyBranding(
    modifier: Modifier = Modifier,
) {
    var visibleLetterCount by remember {
        mutableIntStateOf(QrezzyBrandingDefaults.Animation.INITIAL_VISIBLE_LETTER_COUNT)
    }
    var isSloganVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(QrezzyBrandingDefaults.Animation.INITIAL_DELAY_MILLIS.milliseconds)

        for (
        visibleCount in QrezzyBrandingDefaults.Animation.FIRST_ANIMATED_LETTER_INDEX..QrezzyBrandingDefaults.logoLetters.size
        ) {
            visibleLetterCount = visibleCount
            delay(QrezzyBrandingDefaults.Animation.LETTER_APPEAR_DELAY_MILLIS.milliseconds)
        }

        delay(QrezzyBrandingDefaults.Animation.SLOGAN_APPEAR_DELAY_MILLIS.milliseconds)
        isSloganVisible = true
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val letterWidth = QrezzyBrandingDefaults.calculateLetterWidth(
            availableWidth = maxWidth,
        )
        val sloganWidth = maxWidth * QrezzyBrandingDefaults.Slogan.WIDTH_FACTOR

        Column(
            modifier = Modifier.width(maxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QrezzyLogo(
                letters = QrezzyBrandingDefaults.logoLetters,
                visibleLetterCount = visibleLetterCount,
                letterWidth = letterWidth,
            )

            Spacer(
                modifier = Modifier.height(QrezzyBrandingDefaults.Slogan.topSpacing),
            )

            QrezzySlogan(
                width = sloganWidth,
                isVisible = isSloganVisible,
            )
        }
    }
}

@Composable
private fun QrezzyLogo(
    letters: List<QrezzyLogoLetterData>,
    visibleLetterCount: Int,
    letterWidth: Dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        letters.forEach { letter ->
            QrezzyLogoLetter(
                resId = letter.resId,
                width = letterWidth,
                isVisible = visibleLetterCount >= letter.visibleAtCount,
            )
        }
    }
}

@Composable
private fun QrezzyLogoLetter(
    resId: Int,
    width: Dp,
    isVisible: Boolean,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) {
            QrezzyBrandingDefaults.Animation.VISIBLE_ALPHA
        } else {
            QrezzyBrandingDefaults.Animation.INVISIBLE_ALPHA
        },
        animationSpec = tween(
            durationMillis = QrezzyBrandingDefaults.Animation.LETTER_FADE_DURATION_MILLIS,
        ),
        label = QrezzyBrandingDefaults.Animation.LETTER_ALPHA_LABEL,
    )

    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .width(width)
            .alpha(animatedAlpha),
    )
}

@Composable
private fun QrezzySlogan(
    width: Dp,
    isVisible: Boolean,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) {
            QrezzyBrandingDefaults.Animation.VISIBLE_ALPHA
        } else {
            QrezzyBrandingDefaults.Animation.INVISIBLE_ALPHA
        },
        animationSpec = tween(
            durationMillis = QrezzyBrandingDefaults.Animation.SLOGAN_FADE_DURATION_MILLIS,
        ),
        label = QrezzyBrandingDefaults.Animation.SLOGAN_ALPHA_LABEL,
    )

    Image(
        painter = painterResource(id = R.drawable.slogan_scan_create_share),
        contentDescription = QrezzyBrandingDefaults.Slogan.CONTENT_DESCRIPTION,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(width)
            .alpha(animatedAlpha)
            .padding(start = QrezzyBrandingDefaults.Slogan.startPadding),
    )
}

@Immutable
private data class QrezzyLogoLetterData(
    val resId: Int,
    val visibleAtCount: Int,
)

private object QrezzyBrandingDefaults {
    val logoLetters = listOf(
        QrezzyLogoLetterData(
            resId = R.drawable.logo_q,
            visibleAtCount = 1,
        ),
        QrezzyLogoLetterData(
            resId = R.drawable.logo_r,
            visibleAtCount = 2,
        ),
        QrezzyLogoLetterData(
            resId = R.drawable.logo_e,
            visibleAtCount = 3,
        ),
        QrezzyLogoLetterData(
            resId = R.drawable.logo_z_1,
            visibleAtCount = 4,
        ),
        QrezzyLogoLetterData(
            resId = R.drawable.logo_z_2,
            visibleAtCount = 5,
        ),
        QrezzyLogoLetterData(
            resId = R.drawable.logo_y,
            visibleAtCount = 6,
        ),
    )

    fun calculateLetterWidth(availableWidth: Dp): Dp {
        return (availableWidth / logoLetters.size)
            .coerceAtMost(Logo.maxLetterWidth)
    }

    object Logo {
        val maxLetterWidth = 60.dp
    }

    object Slogan {
        const val WIDTH_FACTOR = 0.65f
        const val CONTENT_DESCRIPTION = "Scan. Create. Share."
        val topSpacing = 12.dp
        val startPadding = 10.dp
    }

    object Animation {
        const val INITIAL_VISIBLE_LETTER_COUNT = 2
        const val FIRST_ANIMATED_LETTER_INDEX = 3
        const val INITIAL_DELAY_MILLIS = 250
        const val LETTER_APPEAR_DELAY_MILLIS = 180
        const val SLOGAN_APPEAR_DELAY_MILLIS = 100
        const val LETTER_FADE_DURATION_MILLIS = 350
        const val SLOGAN_FADE_DURATION_MILLIS = 600
        const val VISIBLE_ALPHA = 1f
        const val INVISIBLE_ALPHA = 0f
        const val LETTER_ALPHA_LABEL = "qrezzy_logo_letter_alpha"
        const val SLOGAN_ALPHA_LABEL = "qrezzy_slogan_alpha"
    }
}