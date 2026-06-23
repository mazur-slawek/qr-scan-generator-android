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
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabs
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.feature.history.components.DeleteQrConfirmationDialog
import software.mazur.qrezzy.feature.history.components.HistoryEmptyAction
import software.mazur.qrezzy.feature.history.components.HistoryListEmpty
import software.mazur.qrezzy.feature.history.components.HistoryListFooter
import software.mazur.qrezzy.feature.history.components.HistoryListItem
import software.mazur.qrezzy.feature.history.components.HistoryListSectionHeader
import software.mazur.qrezzy.feature.history.components.HistorySearchBar
import software.mazur.qrezzy.feature.history.mapper.historyTabs
import software.mazur.qrezzy.feature.history.model.HistorySectionUi

@Composable
fun HistoryScreen(
    onHistoryItemClick: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
    onEmptyActionClick: (HistoryEmptyAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTabItem.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()


    if (uiState.isDeleteConfirmationVisible) {
        DeleteQrConfirmationDialog(
            count = uiState.selectedItemIds.size,
            onCancelClick = viewModel::onDeleteConfirmationDialogDismiss,
            onConfirmClick = viewModel::onDeleteConfirmationDialogConfirm
        )
    }

    Column(modifier = Modifier.padding(horizontal = HistoryScreenDefaults.screenPadding)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_history,
            subtitleResId = R.string.navigation_subtitle_history,
        ) {
            AnimatedContent(
                targetState = uiState.isDeleteModeEnabled,
                transitionSpec = {
                    (fadeIn(
                        animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_IN_DURATION_MILLIS),
                    ) + scaleIn(
                        initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE,
                        animationSpec =
                            tween(HistoryScreenDefaults.TopBarAnimation.SCALE_IN_DURATION_MILLIS),
                    )).togetherWith(
                        fadeOut(
                            animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_OUT_DURATION_MILLIS),
                        ) + scaleOut(
                            targetScale = HistoryScreenDefaults.TopBarAnimation.TARGET_SCALE,
                            animationSpec =
                                tween(HistoryScreenDefaults.TopBarAnimation.SCALE_OUT_DURATION_MILLIS),
                        ),
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
                                animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.DELETE_BUTTON_DELAY_MILLIS),
                            ) + scaleIn(initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE),
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

        HistorySearchBar(
            query = searchQuery,
            enabled = !uiState.isDeleteModeEnabled,
            onQueryChange = viewModel::onSearchQueryChange,
            onClearClick = viewModel::onClearSearchQuery,
        )

        Spacer(modifier = Modifier.height(HistoryScreenDefaults.searchBarTabBarPadding))

        QrezzyTabs(
            tabs = historyTabs,
            selectedTab = selectedTab,
            onSelect = { tab -> viewModel.onTabSelected(tab.key) },
            enabled = !uiState.isDeleteModeEnabled,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isInitialLoading) {
                CircularProgressIndicator()
            } else if (uiState.sections.isEmpty()) {
                HistoryListEmpty(selectedTab = selectedTab, onEmptyActionClick = onEmptyActionClick)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = HistoryScreenDefaults.screenPadding),
                ) {
                    uiState.sections.forEach { section ->
                        stickyHeader { HistoryListSectionHeader(text = section.date) }
                        item(key = section.date) {
                            HistorySection(
                                section = section,
                                isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                                selectedItemIds = uiState.selectedItemIds,
                                onHistoryItemClick = { qrId ->
                                    if (uiState.isDeleteModeEnabled) {
                                        viewModel.onHistoryItemClick(qrId)
                                    } else {
                                        onHistoryItemClick(qrId)
                                    }
                                },
                            )
                        }
                    }
                    if (searchQuery == "") item {
                        Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing * 2))
                        HistoryListFooter()
                        Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing * 2))
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorySection(
    section: HistorySectionUi,
    isDeleteModeEnabled: Boolean,
    selectedItemIds: Set<Long>,
    onHistoryItemClick: (Long) -> Unit,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = BorderLight,
                    shape = HistoryScreenDefaults.Section.shape,
                    width = HistoryScreenDefaults.Section.borderWidth,
                )
                .clip(HistoryScreenDefaults.Section.shape),
        ) {
            section.items.forEachIndexed { index, qr ->
                HistoryListItem(
                    qr = qr,
                    isDeleteModeEnabled = isDeleteModeEnabled,
                    isSelected = qr.id in selectedItemIds,
                    onClick = { onHistoryItemClick(qr.id) }
                )

                if (index != section.items.lastIndex) {
                    HorizontalDivider(
                        color = BorderLight,
                        thickness = HistoryScreenDefaults.Section.dividerThickness,
                        modifier = Modifier.padding(start = HistoryScreenDefaults.Section.dividerStartPadding)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing))
    }
}

object HistoryScreenDefaults {
    val screenPadding = 16.dp
    val searchBarTabBarPadding = 12.dp
    val topBarActionSpacing = 8.dp
    val sectionSpacing = 7.dp

    object Section {
        val shape = ShapeDefaults.Medium
        val borderWidth = 1.dp
        val dividerThickness = 1.dp
        val dividerStartPadding = 70.dp
    }

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