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

@Composable
fun ClearAllHistoryScreen(onBackClick: () -> Unit) {
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
                                value = "324",
                                iconBackgroundColor = QrezzyPinkDark,
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.qrezzy_calendar),
                                iconSize = ClearAllHistoryScreenDefaults.iconSize,
                                title = stringResource(R.string.clear_history_oldest_item_title),
                                value = "24.06.2026",
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
            onClick = {},
            elevation = 0.dp,
            containerColor = QrezzyPink,
            depthColor = QrezzyYellowDark,
        )
        Spacer(modifier = Modifier.height(ClearAllHistoryScreenDefaults.contentPadding))
    }
}


private object ClearAllHistoryScreenDefaults {
    const val STARS_COUNT = 150
    val contentPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}