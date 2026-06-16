package software.mazur.qrezzy.feature.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabs
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.feature.history.components.HistoryListEmpty
import software.mazur.qrezzy.feature.history.components.HistoryListItem
import software.mazur.qrezzy.feature.history.components.HistoryListSectionHeader
import software.mazur.qrezzy.feature.history.mapper.historyTabs

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTabItem.collectAsState()

    Column(modifier = Modifier.padding(horizontal = HistoryScreenDefaults.screenPadding)) {
        QrezzyTopBar(title = stringResource(R.string.navigation_title_history)) {
            AnimatedContent(
                targetState = uiState.isDeleteModeEnabled,
                transitionSpec = {
                    (fadeIn(
                        animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_IN_DURATION_MILLIS)) +
                            scaleIn(
                                initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE,
                                animationSpec = tween(
                                    HistoryScreenDefaults.TopBarAnimation.SCALE_IN_DURATION_MILLIS),
                            )
                            ).togetherWith(
                            fadeOut(
                                animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_OUT_DURATION_MILLIS),
                            ) + scaleOut(
                                targetScale = HistoryScreenDefaults.TopBarAnimation.TARGET_SCALE,
                                animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.SCALE_OUT_DURATION_MILLIS),
                            )
                        ).using(SizeTransform(clip = false))
                },
                label = "history_top_bar_actions_animation",
            ) { isDeleteModeEnabled ->
                if (isDeleteModeEnabled) {
                    Row {
                        QrezzyTopBarButton(onClick = viewModel::onExitDeleteMode, icon = Icons.Outlined.Close)
                        Spacer(modifier = Modifier.width(HistoryScreenDefaults.topBarActionSpacing))
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(
                                animationSpec = tween(
                                    HistoryScreenDefaults.TopBarAnimation.DELETE_BUTTON_DELAY_MILLIS,
                                ),
                            ) + scaleIn(
                                initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE,
                            ),
                        ) {
                            QrezzyTopBarButton(
                                onClick = viewModel::onDeleteSelected,
                                enabled = uiState.canDeleteSelected,
                                icon = Icons.Outlined.DeleteForever,
                            )
                        }
                    }
                } else {
                    QrezzyTopBarButton(
                        enabled = uiState.sections.isNotEmpty(),
                        onClick = viewModel::onEnterDeleteMode,
                        text = stringResource(R.string.history_select),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(HistoryScreenDefaults.screenPadding))

        QrezzyTabs(
            tabs = historyTabs,
            selectedTab = selectedTab,
            onSelect = { tab -> viewModel.onTabSelected(tab.key) },
            modifier = Modifier
                .alpha(if (uiState.isDeleteModeEnabled) 0.4f else 1f)
                .padding(bottom = HistoryScreenDefaults.tabsBottomPadding)
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState.isInitialLoading) CircularProgressIndicator()
            else if (uiState.sections.isEmpty()) HistoryListEmpty()
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = HistoryScreenDefaults.screenPadding)
                ) {
                    uiState.sections.forEach { section ->
                        stickyHeader {
                            HistoryListSectionHeader(text = section.date)
                        }

                        items(count = section.items.size, key = { index -> section.items[index].id }) { index ->
                            val item = section.items[index]

                            HistoryListItem(
                                item = item,
                                isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                                isSelected = item.id in uiState.selectedItemIds,
                                onClick = { viewModel.onHistoryItemClick(item.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

object HistoryScreenDefaults {
    val screenPadding = 16.dp
    val topBarActionSpacing = 8.dp
    val tabsBottomPadding = 2.dp

    object TopBarAnimation {
        const val FADE_IN_DURATION_MILLIS = 180
        const val FADE_OUT_DURATION_MILLIS = 120
        const val SCALE_IN_DURATION_MILLIS = 220
        const val SCALE_OUT_DURATION_MILLIS = 120
        const val DELETE_BUTTON_DELAY_MILLIS = 120
        const val INITIAL_SCALE = 0.92f
        const val TARGET_SCALE = 0.96f
    }
}