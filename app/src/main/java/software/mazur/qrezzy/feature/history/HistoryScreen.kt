package software.mazur.qrezzy.feature.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabs
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.feature.history.components.HistoryList
import software.mazur.qrezzy.feature.history.mapper.historyTabs

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val sections by viewModel.historySections.collectAsState()
    val selectedTab by viewModel.selectedTabItem.collectAsState()
    var isDeleteModeEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = HistoryScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(title = stringResource(R.string.navigation_title_history)) {
            if (isDeleteModeEnabled) {
                QrezzyTopBarButton(
                    onClick = { isDeleteModeEnabled = false },
                    icon = Icons.Outlined.Close,
                )

                Spacer(modifier = Modifier.width(HistoryScreenDefaults.topBarActionSpacing))

                QrezzyTopBarButton(
                    onClick = {},
                    icon = Icons.Outlined.DeleteOutline,
                )
            } else {
                QrezzyTopBarButton(
                    enabled = sections.isNotEmpty(),
                    onClick = { isDeleteModeEnabled = true },
                    text = stringResource(R.string.history_select),
                )
            }
        }

        Spacer(modifier = Modifier.height(HistoryScreenDefaults.topBarTabsSpacing))

        QrezzyTabs(
            tabs = historyTabs,
            selectedTab = selectedTab,
            onSelect = { tab -> viewModel.onTabSelected(tab.key) },
            modifier = Modifier.padding(bottom = HistoryScreenDefaults.tabsBottomPadding)
        )

        HistoryList(sections = sections, isDeleteModeEnabled = isDeleteModeEnabled)
    }
}

object HistoryScreenDefaults {
    val topBarActionSpacing = 8.dp
    val horizontalPadding = 16.dp
    val topBarTabsSpacing = 16.dp
    val tabsBottomPadding = 2.dp
}