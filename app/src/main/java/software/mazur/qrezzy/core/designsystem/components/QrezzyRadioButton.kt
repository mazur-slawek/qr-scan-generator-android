package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark


@Composable
fun QrezzyRadioButton(selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Box(
            modifier = modifier
                .size(QrezzyRadioButtonDefaults.size)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = QrezzyMintDark,
                    unselectedColor = MaterialTheme.colorScheme.surfaceTint),
            )
        }
    }
}

private object QrezzyRadioButtonDefaults {
    val size = 32.dp
}