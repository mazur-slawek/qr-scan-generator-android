package software.mazur.qrezzy.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.Purple

@Composable
fun SettingsScreen() {
    Column {
        QrezzyTopBar(
            title = stringResource(R.string.navigation_title_settings),
            rightButton = QrezzyTopBarButton(
                icon = Icons.Default.Favorite,
                iconTint = Purple,
                onClick = {}
            )
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text("SettingsScreen")
        }
    }
}
