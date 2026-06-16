package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.feature.history.model.HistorySectionUi

@Composable
fun HistoryList(sections: List<HistorySectionUi>, isDeleteModeEnabled: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (!sections.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = HistoryListDefaults.bottomPadding),
            ) {
                sections.forEach { section ->
                    stickyHeader {
                        HistoryListSectionHeader(text = section.date)
                    }
                    items(count = section.items.size, key = { index -> section.items[index].id }) { index ->
                        HistoryListItem(item = section.items[index], isDeleteModeEnabled = isDeleteModeEnabled)
                    }
                }
            }
        } else {
            HistoryListEmpty()
        }
    }
}

private object HistoryListDefaults {
    val bottomPadding = 16.dp
}
