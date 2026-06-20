package software.mazur.qrezzy.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_settings,
            subtitleResId = R.string.navigation_subtitle_settings
        ) {
            QrezzyTopBarButton(
                onClick = {},
                enabled = false,
                icon = Icons.Default.Favorite,
                iconTint = QrezzyPurpleDark,
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
        }
    }
}
