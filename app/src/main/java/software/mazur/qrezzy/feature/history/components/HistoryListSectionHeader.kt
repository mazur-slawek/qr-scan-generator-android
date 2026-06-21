package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun HistoryListSectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = TextSecondary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        maxLines = HistoryListSectionHeaderDefaults.MAX_LINES,
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = HistoryListSectionHeaderDefaults.topPadding,
                    start = HistoryListSectionHeaderDefaults.startPadding,
                    bottom = HistoryListSectionHeaderDefaults.bottomPadding,
                ),
    )
}

private object HistoryListSectionHeaderDefaults {
    val topPadding = 10.dp
    val startPadding = 4.dp
    val bottomPadding = 7.dp
    const val MAX_LINES = 1
}
