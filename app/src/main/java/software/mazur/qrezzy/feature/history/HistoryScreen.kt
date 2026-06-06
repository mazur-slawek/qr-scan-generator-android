package software.mazur.qrezzy.feature.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton

@Composable
fun HistoryScreen() {
    Column {
        QrezzyTopBar(
            title = stringResource(R.string.navigation_title_history),
            rightButton = QrezzyTopBarButton(
                icon = Icons.Outlined.Delete,
                iconTint = Color.Gray,
                enabled = false,
                onClick = {}
            )
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text("HistoryScreen")
        }
    }
}
