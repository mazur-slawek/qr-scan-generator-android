package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class QrezzyTopBarButton(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val iconTint: Color = Color.Black,
    val enabled: Boolean = true,
)

@Composable
fun QrezzyTopBar(
    title: String,
    leftButton: QrezzyTopBarButton? = null,
    rightButton: QrezzyTopBarButton? = null,
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 74.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 2,
            text = title
        )
        leftButton?.let {button ->
            QrezzySmallButton(
                onClick = button.onClick,
                icon = button.icon,
                enabled = button.enabled,
                iconTint = button.iconTint,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart),
            )
        }
        rightButton?.let {button ->
            QrezzySmallButton(
                onClick = button.onClick,
                icon = button.icon,
                enabled = button.enabled,
                iconTint = button.iconTint,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd),
            )
        }
    }
}