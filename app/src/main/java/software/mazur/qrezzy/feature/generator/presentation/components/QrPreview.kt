package software.mazur.qrezzy.feature.generator.presentation.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.theme.Surface

@Composable
fun QrPreview(qrBitmap: Bitmap?, modifier: Modifier = Modifier) {
    val hasQr = qrBitmap != null
    val latestQrBitmap by rememberUpdatedState(qrBitmap)
    Card(
        modifier = modifier,
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = QrPreviewDefaults.Container.elevation),
    ) {
        QrezzyAnimatedStars(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = hasQr,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(250),
                        ) togetherWith fadeOut(
                            animationSpec = tween(200),
                        ) using SizeTransform(
                            clip = false,
                        )
                    },
                    contentAlignment = Alignment.Center,
                    label = "QrPreviewAnimation",
                ) { visible ->
                    if (visible) {
                        latestQrBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code preview",
                            )
                        }
                    }
                }
            }
        }
    }
}

private object QrPreviewDefaults {
    object Container {
        val elevation = 2.dp
    }
}