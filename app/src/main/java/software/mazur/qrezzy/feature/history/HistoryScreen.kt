package software.mazur.qrezzy.feature.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarAction

@Composable
fun HistoryScreen() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        QrezzyTopBar(
            title = stringResource(R.string.navigation_title_history),
            rightAction = QrezzyTopBarAction(
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
        }
    }
}
