package software.mazur.qrezzy.core.designsystem.components.qrezzyQr

import android.graphics.Bitmap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun QrezzyQrPreview(qrBitmap: Bitmap?, modifier: Modifier = Modifier) {
    val latestQrBitmap by rememberUpdatedState(qrBitmap)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = QrezzyQrPreviewDefaults.Container.topPadding,
                bottom = QrezzyQrPreviewDefaults.Container.bottomPadding,
            )
            .height(QrezzyQrPreviewDefaults.Container.height),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = QrezzyQrPreviewDefaults.Container.elevation,
        ),
    ) {
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = latestQrBitmap != null,
                    enter = fadeIn(
                        animationSpec = tween(QrezzyQrPreviewDefaults.Animation.FADE_IN_DURATION_MILLIS),
                    ),
                    exit = fadeOut(
                        animationSpec = tween(QrezzyQrPreviewDefaults.Animation.FADE_OUT_DURATION_MILLIS),
                    ),
                    label = "qr_preview_visibility_animation",
                ) {
                    latestQrBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = QrezzyQrPreviewDefaults.ContentDescription.QR_PREVIEW,
                        )
                    }
                }
            }
        }
    }
}

private object QrezzyQrPreviewDefaults {
    object Container {
        val height = 220.dp
        val elevation = 2.dp
        val topPadding = 16.dp
        val bottomPadding = 2.dp
    }

    object Animation {
        const val FADE_IN_DURATION_MILLIS = 250
        const val FADE_OUT_DURATION_MILLIS = 200
    }

    object ContentDescription {
        const val QR_PREVIEW = "QR Code preview"
    }
}