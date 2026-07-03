package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyRadioButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun MaxHistoryItemsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = MaxHistoryItemsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.settings_history_limit_screen_title)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = MaxHistoryItemsScreenDefaults.STAR_COUNT,
                    modifier = Modifier.height(MaxHistoryItemsScreenDefaults.imageHeight),
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_max_items),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(MaxHistoryItemsScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.history_limit_section_title)) {
                    Column {
                        MaxHistoryItemsScreenDefaults.options.forEachIndexed { index, option ->
                            SettingsItem(
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

private object MaxHistoryItemsScreenDefaults {
    const val STAR_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
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