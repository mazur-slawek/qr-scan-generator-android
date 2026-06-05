package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import kotlin.random.Random

/**
 * Zakres przezroczystości wykorzystywany podczas animowania blob-ów.
 */
@Immutable
data class QrezzyBackgroundAlphaRange(
    val min: Float = 0.45f,
    val max: Float = 0.95f,
)

/**
 * Proporcje pojedynczego blob-a.
 * `widthFactor` i `heightFactor` określają, jak bardzo blob jest rozciągnięty w poziomie i pionie.
 */
private data class BlobShape(
    val widthFactor: Float,
    val heightFactor: Float,
)

/**
 * Konfiguracja animacji pojedynczego blob-a.
 * Zawiera początkowe oraz docelowe wartości animowanych parametrów.
 */
private data class BlobAnimationConfig(
    val initialOffsetY: Float,
    val targetOffsetY: Float,
    val initialScale: Float,
    val targetScale: Float,
    val initialAlpha: Float,
    val targetAlpha: Float,
)

/**
 * Czasy trwania animacji dla pojedynczego blob-a.
 */
private data class BlobAnimationDurations(
    val offsetMillis: Int,
    val scaleMillis: Int,
    val alphaMillis: Int,
)

/**
 * Aktualny stan animowanego blob-a.
 * Przechowuje kształt oraz bieżące wartości animacji wykorzystywane podczas rysowania na Canvas.
 */
private data class AnimatedBlob(
    val shape: BlobShape,
    val offsetY: Float,
    val scale: Float,
    val alpha: Float,
)

@Composable
fun QrezzyAnimatedBackground(
    modifier: Modifier = Modifier,
    leftColor: Color = QrezzyPurple,
    rightColor: Color = QrezzyMint,
    centerGap: Dp = 10.dp,
    blur: Dp = 52.dp,
    alphaRange: QrezzyBackgroundAlphaRange = QrezzyBackgroundAlphaRange(),
) {
    val transition =
        rememberInfiniteTransition(
            label = "qrezzy_background_transition",
        )

    val leftPrimaryBlob =
        rememberAnimatedBlob(
            transition = transition,
            alphaRange = alphaRange,
            durations = BlobDurations.LeftPrimary,
            labelPrefix = "left_primary",
        )

    val leftSecondaryBlob =
        rememberAnimatedBlob(
            transition = transition,
            alphaRange = alphaRange,
            durations = BlobDurations.LeftSecondary,
            alphaMultiplier = BlobAlpha.SECONDARY_MULTIPLIER,
            labelPrefix = "left_secondary",
        )

    val rightPrimaryBlob =
        rememberAnimatedBlob(
            transition = transition,
            alphaRange = alphaRange,
            durations = BlobDurations.RightPrimary,
            labelPrefix = "right_primary",
        )

    val rightSecondaryBlob =
        rememberAnimatedBlob(
            transition = transition,
            alphaRange = alphaRange,
            durations = BlobDurations.RightSecondary,
            alphaMultiplier = BlobAlpha.SECONDARY_MULTIPLIER,
            labelPrefix = "right_secondary",
        )

    Canvas(
        modifier =
            modifier
                .fillMaxSize()
                .blur(blur),
    ) {
        val gapPx = centerGap.toPx()
        val halfWidth = size.width / 2f

        val leftMaxX = halfWidth - gapPx / 2f
        val rightMinX = halfWidth + gapPx / 2f

        val blobBaseHeight = size.height * BlobLayout.BASE_HEIGHT_FACTOR

        clipRect(
            left = 0f,
            top = 0f,
            right = leftMaxX,
            bottom = size.height,
        ) {
            drawBlob(
                color = leftColor.copy(alpha = leftPrimaryBlob.alpha),
                blob = leftPrimaryBlob,
                baseHeight = blobBaseHeight,
                visibleFactor = BlobLayout.LEFT_PRIMARY_VISIBLE_FACTOR,
                centerX = -blobBaseHeight * BlobLayout.LEFT_PRIMARY_X_FACTOR,
                centerY =
                    size.height *
                        (BlobLayout.LEFT_PRIMARY_CENTER_Y_FACTOR + leftPrimaryBlob.offsetY),
            )

            drawBlob(
                color = leftColor.copy(alpha = leftSecondaryBlob.alpha),
                blob = leftSecondaryBlob,
                baseHeight = blobBaseHeight,
                visibleFactor = BlobLayout.LEFT_SECONDARY_VISIBLE_FACTOR,
                centerX = leftMaxX * BlobLayout.LEFT_SECONDARY_CENTER_X_FACTOR,
                centerY =
                    size.height *
                        (BlobLayout.LEFT_SECONDARY_CENTER_Y_FACTOR + leftSecondaryBlob.offsetY),
            )
        }

        clipRect(
            left = rightMinX,
            top = 0f,
            right = size.width,
            bottom = size.height,
        ) {
            drawBlob(
                color = rightColor.copy(alpha = rightPrimaryBlob.alpha),
                blob = rightPrimaryBlob,
                baseHeight = blobBaseHeight,
                visibleFactor = BlobLayout.RIGHT_PRIMARY_VISIBLE_FACTOR,
                centerX = size.width + blobBaseHeight * BlobLayout.RIGHT_PRIMARY_X_FACTOR,
                centerY =
                    size.height *
                        (BlobLayout.RIGHT_PRIMARY_CENTER_Y_FACTOR + rightPrimaryBlob.offsetY),
            )

            drawBlob(
                color = rightColor.copy(alpha = rightSecondaryBlob.alpha),
                blob = rightSecondaryBlob,
                baseHeight = blobBaseHeight,
                visibleFactor = BlobLayout.RIGHT_SECONDARY_VISIBLE_FACTOR,
                centerX =
                    rightMinX +
                        (size.width - rightMinX) * BlobLayout.RIGHT_SECONDARY_CENTER_X_FACTOR,
                centerY =
                    size.height *
                        (BlobLayout.RIGHT_SECONDARY_CENTER_Y_FACTOR + rightSecondaryBlob.offsetY),
            )
        }
    }
}

/**
 * Tworzy i zapamiętuje pojedynczy animowany blob.
 *
 * Kształt oraz parametry animacji są losowane tylko raz,
 * dzięki czemu blob zachowuje spójny wygląd przez cały czas życia
 * komponentu.
 * Parametr `alphaMultiplier` pozwala dodatkowo osłabić widoczność
 * wybranych blob-ów (np. pomocniczych).
 */
@Composable
private fun rememberAnimatedBlob(
    transition: InfiniteTransition,
    alphaRange: QrezzyBackgroundAlphaRange,
    durations: BlobAnimationDurations,
    labelPrefix: String,
    alphaMultiplier: Float = 1f,
): AnimatedBlob {
    val shape =
        remember {
            randomBlobShape()
        }

    val animationConfig =
        remember {
            randomBlobAnimationConfig(alphaRange)
        }

    val offsetY =
        transition.animateFloat(
            initialValue = animationConfig.initialOffsetY,
            targetValue = animationConfig.targetOffsetY,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = durations.offsetMillis),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "${labelPrefix}_blob_offset_y",
        )

    val scale =
        transition.animateFloat(
            initialValue = animationConfig.initialScale,
            targetValue = animationConfig.targetScale,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = durations.scaleMillis),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "${labelPrefix}_blob_scale",
        )

    val alpha =
        transition.animateFloat(
            initialValue = animationConfig.initialAlpha * alphaMultiplier,
            targetValue = animationConfig.targetAlpha * alphaMultiplier,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = durations.alphaMillis),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "${labelPrefix}_blob_alpha",
        )

    return AnimatedBlob(
        shape = shape,
        offsetY = offsetY.value,
        scale = scale.value,
        alpha = alpha.value,
    )
}

/**
 * Rysuje pojedynczy blob na Canvas.
 *
 * Rozmiar blob-a wyliczany jest na podstawie bazowej wysokości,
 * proporcji kształtu oraz aktualnej skali animacji.
 * Parametr `visibleFactor` pozwala kontrolować,
 * jaka część blob-a pozostaje widoczna na ekranie.
 */
private fun DrawScope.drawBlob(
    color: Color,
    blob: AnimatedBlob,
    baseHeight: Float,
    visibleFactor: Float,
    centerX: Float,
    centerY: Float,
) {
    val blobWidth = baseHeight * blob.shape.widthFactor * blob.scale
    val blobHeight = baseHeight * blob.shape.heightFactor * blob.scale

    drawOval(
        color = color,
        topLeft =
            Offset(
                x = centerX - blobWidth * visibleFactor,
                y = centerY - blobHeight / 2f,
            ),
        size =
            Size(
                width = blobWidth,
                height = blobHeight,
            ),
    )
}

/**
 * Losuje wertykalny kształt plamy.
 *
 * Szerokość jest celowo mniejsza, a wysokość większa,
 * żeby plamy wyglądały jak miękkie pionowe owale przy krawędziach ekranu.
 */
private fun randomBlobShape(): BlobShape =
    BlobShape(
        widthFactor =
            randomFloat(
                min = BlobShapeConfig.RANDOM_WIDTH_FACTOR_MIN,
                max = BlobShapeConfig.RANDOM_WIDTH_FACTOR_MAX,
            ),
        heightFactor =
            randomFloat(
                min = BlobShapeConfig.RANDOM_HEIGHT_FACTOR_MIN,
                max = BlobShapeConfig.RANDOM_HEIGHT_FACTOR_MAX,
            ),
    )

/**
 * Losuje początkowe i docelowe wartości animacji.
 *
 * Dzięki temu każde uruchomienie ekranu może wyglądać trochę inaczej,
 * ale wartości nadal mieszczą się w bezpiecznych zakresach.
 */
private fun randomBlobAnimationConfig(alphaRange: QrezzyBackgroundAlphaRange): BlobAnimationConfig =
    BlobAnimationConfig(
        initialOffsetY =
            randomFloat(
                min = BlobAnimationValue.OFFSET_Y_MIN,
                max = BlobAnimationValue.OFFSET_Y_MAX,
            ),
        targetOffsetY =
            randomFloat(
                min = BlobAnimationValue.OFFSET_Y_MIN,
                max = BlobAnimationValue.OFFSET_Y_MAX,
            ),
        initialScale =
            randomFloat(
                min = BlobAnimationValue.SCALE_MIN,
                max = BlobAnimationValue.SCALE_MAX,
            ),
        targetScale =
            randomFloat(
                min = BlobAnimationValue.SCALE_MIN,
                max = BlobAnimationValue.SCALE_MAX,
            ),
        initialAlpha =
            randomFloat(
                min = alphaRange.min,
                max = alphaRange.max,
            ),
        targetAlpha =
            randomFloat(
                min = alphaRange.min,
                max = alphaRange.max,
            ),
    )

/**
 * Generuje losową wartość Float z zakresu min max.
 *
 * Wykorzystywane do losowania parametrów animacji blob-ów,
 * dzięki czemu każde uruchomienie aplikacji może wyglądać nieco inaczej.
 */
private fun randomFloat(
    min: Float,
    max: Float,
): Float =
    Random
        .nextDouble(
            from = min.toDouble(),
            until = max.toDouble(),
        ).toFloat()

/**
 * Czasy trwania poszczególnych animacji blob-ów.
 *
 * Różne wartości sprawiają, że tło wygląda bardziej naturalnie
 * i nie porusza się synchronicznie.
 */
private object BlobDurations {
    val LeftPrimary =
        BlobAnimationDurations(
            offsetMillis = 7_600,
            scaleMillis = 9_000,
            alphaMillis = 6_800,
        )

    val LeftSecondary =
        BlobAnimationDurations(
            offsetMillis = 9_200,
            scaleMillis = 11_000,
            alphaMillis = 8_800,
        )

    val RightPrimary =
        BlobAnimationDurations(
            offsetMillis = 8_200,
            scaleMillis = 8_600,
            alphaMillis = 7_200,
        )

    val RightSecondary =
        BlobAnimationDurations(
            offsetMillis = 10_400,
            scaleMillis = 10_800,
            alphaMillis = 9_600,
        )
}

/**
 * Zakres losowanych wartości animacji.
 */
private object BlobAnimationValue {
    const val OFFSET_Y_MIN = -0.05f
    const val OFFSET_Y_MAX = 0.05f

    const val SCALE_MIN = 0.75f
    const val SCALE_MAX = 1.18f
}

/**
 * Dodatkowe osłabienie przezroczystości dla pomocniczych blob-ów.
 */
private object BlobAlpha {
    const val SECONDARY_MULTIPLIER = 0.75f
}

/**
 * Parametry rozmieszczenia blob-ów na ekranie.
 */
private object BlobLayout {
    /** Bazowy rozmiar blob-a względem wysokości ekranu. */
    const val BASE_HEIGHT_FACTOR = 0.34f

    /** Wysunięcie głównych blob-ów poza ekran. */
    const val LEFT_PRIMARY_X_FACTOR = 0.18f
    const val RIGHT_PRIMARY_X_FACTOR = 0.16f

    /** Pozycje pionowe blob-ów. */
    const val LEFT_PRIMARY_CENTER_Y_FACTOR = 0.58f
    const val LEFT_SECONDARY_CENTER_Y_FACTOR = 0.24f

    const val RIGHT_PRIMARY_CENTER_Y_FACTOR = 0.22f
    const val RIGHT_SECONDARY_CENTER_Y_FACTOR = 0.70f

    /** Pozycje poziome pomocniczych blob-ów. */
    const val LEFT_SECONDARY_CENTER_X_FACTOR = 0.28f
    const val RIGHT_SECONDARY_CENTER_X_FACTOR = 0.74f

    /** Określa, jaka część blob-a jest widoczna na ekranie. */
    const val LEFT_PRIMARY_VISIBLE_FACTOR = 0.45f
    const val LEFT_SECONDARY_VISIBLE_FACTOR = 0.50f

    const val RIGHT_PRIMARY_VISIBLE_FACTOR = 0.55f
    const val RIGHT_SECONDARY_VISIBLE_FACTOR = 0.50f
}

/**
 * Zakres losowanych proporcji blob-ów.
 *
 * Większa wysokość względem szerokości daje efekt
 * pionowych, miękkich gradientów przy krawędziach ekranu.
 */
private object BlobShapeConfig {
    const val RANDOM_WIDTH_FACTOR_MIN = 0.45f
    const val RANDOM_WIDTH_FACTOR_MAX = 0.75f

    const val RANDOM_HEIGHT_FACTOR_MIN = 2.20f
    const val RANDOM_HEIGHT_FACTOR_MAX = 3.20f
}
