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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabs
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.feature.history.components.DeleteQrConfirmationDialog
import software.mazur.qrezzy.feature.history.components.HistoryEmptyAction
import software.mazur.qrezzy.feature.history.components.HistoryListEmpty
import software.mazur.qrezzy.feature.history.components.HistoryListFooter
import software.mazur.qrezzy.feature.history.components.HistoryListItem
import software.mazur.qrezzy.feature.history.components.HistoryListSectionHeader
import software.mazur.qrezzy.feature.history.components.HistorySearchBar
import software.mazur.qrezzy.feature.history.mapper.historyTabs
import software.mazur.qrezzy.feature.history.model.HistoryUiState

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
            onConfirmClick = viewModel::onDeleteConfirmationDialogConfirm,
        )
    }

    Column(modifier = Modifier.padding(horizontal = HistoryScreenDefaults.screenPadding)) {
        HistoryTopBar(
            isDeleteModeEnabled = uiState.isDeleteModeEnabled,
            canDeleteSelected = uiState.canDeleteSelected,
            hasItems = uiState.hasItems,
            onEnterDeleteMode = viewModel::onEnterDeleteMode,
            onExitDeleteMode = viewModel::onExitDeleteMode,
            onDeleteSelected = viewModel::onDeleteSelected,
        )
        Spacer(modifier = Modifier.height(HistoryScreenDefaults.topBarSearchSpacing))
        HistorySearchBar(
            query = searchQuery,
            enabled = !uiState.isDeleteModeEnabled,
            onQueryChange = viewModel::onSearchQueryChange,
            onClearClick = viewModel::onClearSearchQuery,
        )
        Spacer(modifier = Modifier.height(HistoryScreenDefaults.searchBarTabBarSpacing))
        QrezzyTabs(
            tabs = historyTabs,
            selectedTab = selectedTab,
            onSelect = { tab -> viewModel.onTabSelected(tab.key) },
            enabled = !uiState.isDeleteModeEnabled,
        )
        HistoryContent(
            uiState = uiState,
            selectedTab = selectedTab,
            searchQuery = searchQuery,
            onHistoryItemClick = onHistoryItemClick,
            onEmptyActionClick = onEmptyActionClick,
            onDeleteModeItemClick = viewModel::onHistoryItemClick,
            onFavoriteClick = viewModel::onFavoriteClick,
            onClearFocus = focusManager::clearFocus,
        )
    }
}

@Composable
private fun HistoryTopBar(
    isDeleteModeEnabled: Boolean,
    canDeleteSelected: Boolean,
    hasItems: Boolean,
    onEnterDeleteMode: () -> Unit,
    onExitDeleteMode: () -> Unit,
    onDeleteSelected: () -> Unit,
) {
    QrezzyTopBar(titleResId = R.string.navigation_title_history, subtitleResId = R.string.navigation_subtitle_history) {
        AnimatedContent(
            targetState = isDeleteModeEnabled,
            transitionSpec = {
                (fadeIn(
                    animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_IN_DURATION_MILLIS),
                ) + scaleIn(
                    initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE,
                    animationSpec = tween(
                        HistoryScreenDefaults.TopBarAnimation.SCALE_IN_DURATION_MILLIS),
                )).togetherWith(
                    fadeOut(
                        animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.FADE_OUT_DURATION_MILLIS),
                    ) + scaleOut(
                        targetScale = HistoryScreenDefaults.TopBarAnimation.TARGET_SCALE,
                        animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.SCALE_OUT_DURATION_MILLIS))
                ).using(SizeTransform(clip = false))
            },
            label = "history_top_bar_actions_animation",
        ) { isDeleteMode ->
            if (isDeleteMode) {
                HistoryDeleteModeActions(
                    canDeleteSelected = canDeleteSelected,
                    onExitDeleteMode = onExitDeleteMode,
                    onDeleteSelected = onDeleteSelected,
                )
            } else {
                QrezzyTopBarButton(
                    enabled = hasItems,
                    onClick = onEnterDeleteMode,
                    iconPainter = painterResource(R.drawable.qrezzy_selection)
                )
            }
        }
    }
}

@Composable
private fun HistoryDeleteModeActions(
    canDeleteSelected: Boolean,
    onExitDeleteMode: () -> Unit,
    onDeleteSelected: () -> Unit,
) {
    Row {
        QrezzyTopBarButton(onClick = onExitDeleteMode, iconPainter = painterResource(R.drawable.qrezzy_close))
        Spacer(modifier = Modifier.width(HistoryScreenDefaults.topBarActionSpacing))
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(
                animationSpec = tween(HistoryScreenDefaults.TopBarAnimation.DELETE_BUTTON_DELAY_MILLIS),
            ) + scaleIn(initialScale = HistoryScreenDefaults.TopBarAnimation.INITIAL_SCALE)
        ) {
            QrezzyTopBarButton(
                onClick = onDeleteSelected,
                enabled = canDeleteSelected,
                iconPainter = painterResource(R.drawable.qrezzy_delete)
            )
        }
    }
}

@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    selectedTab: software.mazur.qrezzy.core.designsystem.components.QrezzyTabItem,
    searchQuery: String,
    onHistoryItemClick: (Long) -> Unit,
    onEmptyActionClick: (HistoryEmptyAction) -> Unit,
    onDeleteModeItemClick: (Long) -> Unit,
    onFavoriteClick: (Qr) -> Unit,
    onClearFocus: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { onClearFocus() }) },
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.isInitialLoading -> {
                CircularProgressIndicator()
            }

            !uiState.hasItems        -> {
                HistoryListEmpty(selectedTab = selectedTab, onEmptyActionClick = onEmptyActionClick)
            }

            else                     -> {
                HistoryList(
                    uiState = uiState,
                    searchQuery = searchQuery,
                    onHistoryItemClick = onHistoryItemClick,
                    onDeleteModeItemClick = onDeleteModeItemClick,
                    onFavoriteClick = onFavoriteClick,
                )
            }
        }
    }
}

@Composable
private fun HistoryList(
    uiState: HistoryUiState,
    searchQuery: String,
    onHistoryItemClick: (Long) -> Unit,
    onDeleteModeItemClick: (Long) -> Unit,
    onFavoriteClick: (Qr) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = HistoryScreenDefaults.screenPadding),
    ) {
        if (uiState.favoriteItems.isNotEmpty()) {
            item(key = HistoryScreenDefaults.Favorites.KEY) {
                Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing))
                Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing))
                HistorySection(
                    items = uiState.favoriteItems,
                    isFavoritesSection = true,
                    isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                    selectedItemIds = uiState.selectedItemIds,
                    onHistoryItemClick = { qrId ->
                        handleHistoryItemClick(
                            qrId = qrId,
                            isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                            onDeleteModeItemClick = onDeleteModeItemClick,
                            onHistoryItemClick = onHistoryItemClick,
                        )
                    },
                    onFavoriteClick = onFavoriteClick,
                    modifier = Modifier.animateItem(),
                )
            }
        }
        uiState.sections.forEach { section ->
            stickyHeader(key = "history_header_${section.date}") {
                HistoryListSectionHeader(text = section.date)
            }
            item(key = "history_section_${section.date}") {
                HistorySection(
                    items = section.items,
                    isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                    selectedItemIds = uiState.selectedItemIds,
                    onHistoryItemClick = { qrId ->
                        handleHistoryItemClick(
                            qrId = qrId,
                            isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                            onDeleteModeItemClick = onDeleteModeItemClick,
                            onHistoryItemClick = onHistoryItemClick,
                        )
                    },
                    onFavoriteClick = onFavoriteClick,
                    modifier = Modifier.animateItem(),
                )
            }
        }
        if (searchQuery.isBlank()) {
            item(key = HistoryScreenDefaults.Footer.KEY) {
                HistoryFooterItem()
            }
        }
    }
}

private fun handleHistoryItemClick(
    qrId: Long,
    isDeleteModeEnabled: Boolean,
    onDeleteModeItemClick: (Long) -> Unit,
    onHistoryItemClick: (Long) -> Unit,
) {
    if (isDeleteModeEnabled) onDeleteModeItemClick(qrId) else onHistoryItemClick(qrId)
}

@Composable
private fun HistorySection(
    items: List<Qr>,
    isDeleteModeEnabled: Boolean,
    selectedItemIds: Set<Long>,
    onHistoryItemClick: (Long) -> Unit,
    onFavoriteClick: (Qr) -> Unit,
    modifier: Modifier = Modifier,
    isFavoritesSection: Boolean = false,
) {
    Column {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    color = BorderLight,
                    shape = HistoryScreenDefaults.Section.shape,
                    width = HistoryScreenDefaults.Section.borderWidth,
                )
                .clip(HistoryScreenDefaults.Section.shape),
        ) {
            if (isFavoritesSection) FavoritesSectionHeader()
            items.forEachIndexed { index, qr ->
                HistoryListItem(
                    qr = qr,
                    isDeleteModeEnabled = isDeleteModeEnabled,
                    isSelected = qr.id in selectedItemIds,
                    onClick = { onHistoryItemClick(qr.id) },
                    onFavoriteClick = { onFavoriteClick(qr) },
                )
                if (index != items.lastIndex) HistorySectionDivider()
            }
        }
        Spacer(modifier = Modifier.height(HistoryScreenDefaults.sectionSpacing))
    }
}

@Composable
private fun FavoritesSectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Surface)
            .padding(
                horizontal = HistoryScreenDefaults.FavoritesHeader.horizontalPadding,
                vertical = HistoryScreenDefaults.FavoritesHeader.verticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HistoryScreenDefaults.FavoritesHeader.iconTextSpacing),
    ) {
        Icon(imageVector = Icons.Default.Star, tint = QrezzyYellowDark, contentDescription = null)
        Text(
            text = stringResource(R.string.history_section_favorites).uppercase(),
            color = TextSecondary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
    HorizontalDivider(
        color = BorderLight,
        thickness = HistoryScreenDefaults.Section.dividerThickness,
    )
}

@Composable
private fun HistorySectionDivider() {
    HorizontalDivider(
        color = BorderLight,
        thickness = HistoryScreenDefaults.Section.dividerThickness,
        modifier = Modifier.padding(start = HistoryScreenDefaults.Section.dividerStartPadding),
    )
}

@Composable
private fun HistoryFooterItem() {
    Spacer(modifier = Modifier.height(HistoryScreenDefaults.footerVerticalSpacing))
    HistoryListFooter()
    Spacer(modifier = Modifier.height(HistoryScreenDefaults.footerVerticalSpacing))
}

object HistoryScreenDefaults {
    val screenPadding = 16.dp
    val topBarSearchSpacing = 16.dp
    val searchBarTabBarSpacing = 12.dp
    val topBarActionSpacing = 8.dp
    val sectionSpacing = 7.dp
    val footerVerticalSpacing = 14.dp

    object Favorites {
        const val KEY = "favorites_section"
    }

    object Footer {
        const val KEY = "history_footer"
    }

    object FavoritesHeader {
        val horizontalPadding = 12.dp
        val verticalPadding = 8.dp
        val iconTextSpacing = 5.dp
    }

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