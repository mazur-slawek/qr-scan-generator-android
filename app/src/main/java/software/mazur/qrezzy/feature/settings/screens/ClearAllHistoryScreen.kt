package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyListItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.ClearHistoryConfirmationDialog
import software.mazur.qrezzy.feature.settings.model.SettingsUiState

@Composable
fun ClearAllHistoryScreen(
    settingsUiState: SettingsUiState,
    itemsCount: Int,
    latestCreatedAt: Long?,
    onBackClick: () -> Unit,
    onClearAllHistoryClick: () -> Unit,
    onClearHistoryConfirmed: () -> Unit,
    onClearHistoryDialogDismissed: () -> Unit,
) {
    if (settingsUiState.showClearHistoryDialog) {
        ClearHistoryConfirmationDialog(
            onConfirmClick = onClearHistoryConfirmed,
            onCancelClick = onClearHistoryDialogDismissed
        )
    }
    Column(modifier = Modifier.padding(horizontal = ClearAllHistoryScreenDefaults.contentPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.clear_history_screen_title)
        QrezzyAnimatedStars(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            starsCount = ClearAllHistoryScreenDefaults.STARS_COUNT
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ClearAllHistoryScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_clear_all_history),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ClearAllHistoryScreenDefaults.imageTopPadding)
                            .height(ClearAllHistoryScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyListSection(title = stringResource(R.string.clear_history_screen_subtitle)) {
                        Column {
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.qrezzy_delete),
                                iconSize = ClearAllHistoryScreenDefaults.iconSize,
                                title = stringResource(R.string.clear_history_current_items_title),
                                value = itemsCount.toString(),
                                iconBackgroundColor = QrezzyPinkDark,
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.qrezzy_calendar),
                                iconSize = ClearAllHistoryScreenDefaults.iconSize,
                                title = stringResource(R.string.clear_history_latest_item_title),
                                value = latestCreatedAt.toDisplayDate(),
                                iconBackgroundColor = QrezzyPurpleDark,
                                showDivider = false,
                            )
                        }
                    }
                }
            }
        }
        QrezzyButton(
            text = stringResource(R.string.clear_history_button),
            onClick = onClearAllHistoryClick,
            enabled = itemsCount > 0,
            elevation = 0.dp,
            containerColor = QrezzyPink,
            depthColor = QrezzyYellowDark,
        )
        Spacer(modifier = Modifier.height(ClearAllHistoryScreenDefaults.contentPadding))
    }
}

private fun Long?.toDisplayDate(): String {
    if (this == null) return "—"
    val formatter = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(this))
}

private object ClearAllHistoryScreenDefaults {
    const val STARS_COUNT = 150
    val contentPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}