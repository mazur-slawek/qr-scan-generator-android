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
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.TextDisabled

@Composable
fun ThemeScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = ThemeScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.theme_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = ThemeScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ThemeScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_theme),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ThemeScreenDefaults.imageTopPadding)
                            .height(ThemeScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyListSection(title = stringResource(R.string.theme_screen_subtitle)) {
                        Column {
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.theme_mode_light),
                                iconSize = ThemeScreenDefaults.iconSize,
                                title = stringResource(R.string.theme_mode_light),
                                trailing = { QrezzyRadioButton(selected = false, onClick = {}) },
                                iconBackgroundColor = TextDisabled.copy(alpha = 0.5f),
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.theme_mode_dark),
                                iconSize = ThemeScreenDefaults.iconSize,
                                title = stringResource(R.string.theme_mode_dark),
                                trailing = { QrezzyRadioButton(selected = false, onClick = {}) },
                                iconBackgroundColor = TextDisabled.copy(alpha = 0.5f),
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.theme_mode_system),
                                iconSize = ThemeScreenDefaults.iconSize,
                                title = stringResource(R.string.theme_mode_system),
                                trailing = { QrezzyRadioButton(selected = true, onClick = {}) },
                                iconBackgroundColor = QrezzyMintDark,
                            )
                        }
                    }
                }
            }
        }
    }
}

private object ThemeScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}