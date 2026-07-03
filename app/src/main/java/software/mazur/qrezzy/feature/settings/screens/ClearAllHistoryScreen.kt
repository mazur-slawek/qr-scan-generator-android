package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun ClearAllHistoryScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = ClearAllHistoryScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.settings_clear_history_screen_title)
        Column(modifier = Modifier.fillMaxSize()) {
            QrezzyAnimatedStars(
                starsCount = ClearAllHistoryScreenDefaults.STAR_COUNT,
                modifier = Modifier.height(ClearAllHistoryScreenDefaults.imageHeight),
            ) {
                Image(
                    painter = painterResource(R.drawable.qrezzy_mascot_clear_all_history),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(ClearAllHistoryScreenDefaults.imagePadding)
                        .fillMaxHeight(),
                )
            }
            QrezzyFieldWrapper(
                title = stringResource(R.string.clear_history_section_title),
                modifier = Modifier.weight(ClearAllHistoryScreenDefaults.CONTENT_WEIGHT),
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Outlined.Delete,
                        iconSize = ClearAllHistoryScreenDefaults.iconSize,
                        title = stringResource(R.string.clear_history_current_items_title),
                        value = "324",
                        iconTintColor = QrezzyPinkDark,
                        iconBackgroundColor = QrezzyPinkDark,
                    )
                    SettingsItem(
                        icon = Icons.Outlined.CalendarMonth,
                        iconSize = ClearAllHistoryScreenDefaults.iconSize,
                        title = stringResource(R.string.clear_history_oldest_item_title),
                        value = "24.06.2026",
                        iconTintColor = QrezzyPinkDark,
                        iconBackgroundColor = QrezzyPinkDark,
                        showDivider = false,
                    )
                }
            }
            QrezzyButton(
                text = stringResource(R.string.clear_history_button),
                onClick = {},
                elevation = ClearAllHistoryScreenDefaults.buttonElevation,
                containerColor = QrezzyPink,
                depthColor = QrezzyYellowDark,
            )
        }
    }
}

private object ClearAllHistoryScreenDefaults {
    const val STAR_COUNT = 50
    const val CONTENT_WEIGHT = 1f
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp
    val buttonElevation = 0.dp
}