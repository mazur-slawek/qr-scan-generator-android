package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sin

@Composable
fun QrezzyAnimatedStars(
    modifier: Modifier = Modifier,
    starsCount: Int = QrezzyAnimatedStarsDefaults.DEFAULTS_STARS_COUNT,
    enabled: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        if (width <= 0f || height <= 0f) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
            return@BoxWithConstraints
        }
        val stars = remember(width, height, starsCount) {
            buildStars(count = starsCount, width = width, height = height)
        }
        var timeMillis by remember { mutableLongStateOf(0L) }

        LaunchedEffect(enabled) {
            if (!enabled) return@LaunchedEffect

            while (true) {
                withFrameMillis { frameTime ->
                    timeMillis = frameTime
                }
            }
        }
        val timeSeconds = if (enabled) {
            timeMillis / QrezzyAnimatedStarsDefaults.TIME_MILLIS_DIVIDER
        } else {
            QrezzyAnimatedStarsDefaults.INITIAL_TIME_SECONDS
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                stars.forEach { star -> drawAnimatedStar(star = star, timeSeconds = timeSeconds) }
            }
            content()
        }
    }
}

private fun buildStars(count: Int, width: Float, height: Float): List<QrezzyStar> {
    val colors = QrezzyAnimatedStarsDefaults.colors
    val safePadding = QrezzyAnimatedStarsDefaults.SAFE_PADDING

    return List(count) { index ->
        val sizeFactor = pseudoRandom(index * 71 + 23)
        val radius = QrezzyAnimatedStarsDefaults.Star.MIN_RADIUS +
                sizeFactor * QrezzyAnimatedStarsDefaults.Star.RADIUS_RANGE
        val minX = safePadding + radius
        val maxX = width - safePadding - radius
        val minY = safePadding + radius
        val maxY = height - safePadding - radius

        QrezzyStar(
            x = minX + pseudoRandom(index * 37 + 11) * (maxX - minX),
            y = minY + pseudoRandom(index * 53 + 17) * (maxY - minY),
            radius = radius,
            color = colors[index % colors.size],
            phase = pseudoRandom(index * 97 + 31) * QrezzyAnimatedStarsDefaults.FULL_CIRCLE_RADIANS,
            blinkSpeed = QrezzyAnimatedStarsDefaults.Animation.MIN_BLINK_SPEED +
                    pseudoRandom(index * 19 + 7) * QrezzyAnimatedStarsDefaults.Animation.BLINK_SPEED_RANGE,
            moveSpeed = QrezzyAnimatedStarsDefaults.Animation.MIN_MOVE_SPEED +
                    pseudoRandom(index * 31 + 9) * QrezzyAnimatedStarsDefaults.Animation.MOVE_SPEED_RANGE,
            offsetX = QrezzyAnimatedStarsDefaults.Animation.MIN_OFFSET +
                    pseudoRandom(index * 43 + 5) * QrezzyAnimatedStarsDefaults.Animation.OFFSET_RANGE,
            offsetY = QrezzyAnimatedStarsDefaults.Animation.MIN_OFFSET +
                    pseudoRandom(index * 29 + 3) * QrezzyAnimatedStarsDefaults.Animation.OFFSET_RANGE,
        )
    }
}

private fun DrawScope.drawAnimatedStar(star: QrezzyStar, timeSeconds: Float) {
    val blinkWave = abs(sin(timeSeconds * star.blinkSpeed + star.phase))
    val moveWaveX = sin(timeSeconds * star.moveSpeed + star.phase)
    val moveWaveY = sin(
        timeSeconds * star.moveSpeed * QrezzyAnimatedStarsDefaults.Animation.Y_MOVE_MULTIPLIER + star.phase)
    val alpha = QrezzyAnimatedStarsDefaults.Animation.MIN_ALPHA +
            QrezzyAnimatedStarsDefaults.Animation.ALPHA_RANGE * blinkWave
    val scale = QrezzyAnimatedStarsDefaults.Animation.MIN_RANGE +
            QrezzyAnimatedStarsDefaults.Animation.SCALE_RANGE * blinkWave
    val animatedX = star.x + star.offsetX * moveWaveX
    val animatedY = star.y + star.offsetY * moveWaveY

    drawStar(
        center = Offset(animatedX, animatedY),
        radius = star.radius * scale,
        color = star.color.copy(alpha = alpha),
    )
}

private fun DrawScope.drawStar(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        cubicTo(
            center.x + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x + radius,
            center.y,
        )
        cubicTo(
            center.x + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x,
            center.y + radius,
        )
        cubicTo(
            center.x - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y + radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x - radius,
            center.y,
        )
        cubicTo(
            center.x - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.y - radius * QrezzyAnimatedStarsDefaults.Star.CURVE_FACTOR,
            center.x,
            center.y - radius,
        )
        close()
    }
    drawPath(
        path = path,
        color = color,
    )
}

private fun pseudoRandom(seed: Int): Float {
    val value = sin(seed * QrezzyAnimatedStarsDefaults.Random.SEED_MULTIPLIER) *
            QrezzyAnimatedStarsDefaults.Random.VALUE_MULTIPLIER
    return value - floor(value)
}

@Immutable
private data class QrezzyStar(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
    val phase: Float,
    val blinkSpeed: Float,
    val moveSpeed: Float,
    val offsetX: Float,
    val offsetY: Float,
)

private object QrezzyAnimatedStarsDefaults {
    const val DEFAULTS_STARS_COUNT = 120
    const val TIME_MILLIS_DIVIDER = 1000f
    const val INITIAL_TIME_SECONDS = 0f
    const val SAFE_PADDING = 32f
    const val FULL_CIRCLE_RADIANS = PI.toFloat() * 2f
    val colors = listOf(
        QrezzyPinkDark,
        QrezzyPurpleDark,
        QrezzyYellowDark,
        QrezzyMintDark,
    )

    object Star {
        const val MIN_RADIUS = 3f
        const val RADIUS_RANGE = 8f
        const val CURVE_FACTOR = 0.14f
    }

    object Animation {
        const val MIN_ALPHA = 0.12f
        const val ALPHA_RANGE = 0.88f
        const val MIN_RANGE = 0.45f
        const val SCALE_RANGE = 0.7f
        const val MIN_BLINK_SPEED = 2.5f
        const val BLINK_SPEED_RANGE = 3.5f
        const val MIN_MOVE_SPEED = 1.2f
        const val MOVE_SPEED_RANGE = 2.2f
        const val MIN_OFFSET = -6f
        const val OFFSET_RANGE = 12f
        const val Y_MOVE_MULTIPLIER = 1.3f
    }

    object Random {
        const val SEED_MULTIPLIER = 12.9898f
        const val VALUE_MULTIPLIER = 43758.5453f
    }
}