package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark

@Composable
fun QrezzySwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = QrezzyMintDark,
                uncheckedThumbColor = Color.White,
                uncheckedBorderColor = MaterialTheme.colorScheme.surfaceTint.copy(
                    alpha = SettingsScreenDefaults.UNCHECKED_SWITCH_ALPHA
                ),
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceTint.copy(
                    alpha = SettingsScreenDefaults.UNCHECKED_SWITCH_ALPHA
                )
            )
        )
    }
}

private object SettingsScreenDefaults {
    const val UNCHECKED_SWITCH_ALPHA = 0.4f
}
