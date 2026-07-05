package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import software.mazur.qrezzy.R

@Composable
fun QrezzyCopyright(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.app_copyright),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}
