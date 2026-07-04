package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import software.mazur.qrezzy.core.designsystem.components.QrezzyListItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzyRadioButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar

@Composable
fun MaxHistoryItemsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = MaxHistoryItemsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.history_limit_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = MaxHistoryItemsScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaxHistoryItemsScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_max_items),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaxHistoryItemsScreenDefaults.imageTopPadding)
                            .height(MaxHistoryItemsScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyListSection(title = stringResource(R.string.history_limit_screen_subtitle)) {
                        Column {
                            MaxHistoryItemsScreenDefaults.options.forEachIndexed { index, option ->
                                QrezzyListItem(
                                    title = stringResource(option.titleResId),
                                    trailing = {
                                        QrezzyRadioButton(
                                            selected = option.selected,
                                            onClick = {},
                                        )
                                    },
                                    showDivider = index != MaxHistoryItemsScreenDefaults.options.lastIndex
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private object MaxHistoryItemsScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val options = listOf(
        HistoryLimitOption(R.string.history_limit_50, false),
        HistoryLimitOption(R.string.history_limit_100, false),
        HistoryLimitOption(R.string.history_limit_200, false),
        HistoryLimitOption(R.string.history_limit_500, true),
        HistoryLimitOption(R.string.history_limit_1000, false),
        HistoryLimitOption(R.string.history_limit_unlimited, false),
    )
}

private data class HistoryLimitOption(
    val titleResId: Int,
    val selected: Boolean,
)