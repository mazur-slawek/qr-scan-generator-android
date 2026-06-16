package software.mazur.qrezzy.feature.history.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar

@Composable
fun HistoryDetailsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = HistoryDetailsScreenDefaults.padding)) {
        QrezzyTopBar(title = "Details", onBackClick = onBackClick)
    }
}

private object HistoryDetailsScreenDefaults {
    val padding = 16.dp
}