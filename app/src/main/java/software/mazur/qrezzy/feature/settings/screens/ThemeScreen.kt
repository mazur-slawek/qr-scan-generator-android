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
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.TextDisabled
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun ThemeScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(ThemeScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_theme_screen_title,
            subtitleResId = R.string.settings_theme_screen_subtitle,
            onBackClick = onBackClick
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = ThemeScreenDefaults.START_COUNT,
                    modifier = Modifier.height(ThemeScreenDefaults.imageHeight)
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_theme),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(ThemeScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }
            item {
                QrezzyFieldWrapper {
                    Column {
                        SettingsItem(
                            iconPainter = painterResource(R.drawable.theme_mode_light),
                            iconSize = ThemeScreenDefaults.iconSize,
                            title = stringResource(R.string.theme_mode_light),
                            trailing = { QrezzyRadioButton(selected = false, onClick = {}) },
                            iconTintColor = TextSecondary,
                            iconBackgroundColor = TextDisabled,
                        )
                        SettingsItem(
                            iconPainter = painterResource(R.drawable.theme_mode_dark),
                            iconSize = ThemeScreenDefaults.iconSize,
                            title = stringResource(R.string.theme_mode_dark),
                            trailing = { QrezzyRadioButton(selected = false, onClick = {}) },
                            iconTintColor = TextSecondary,
                            iconBackgroundColor = TextDisabled,
                        )
                        SettingsItem(
                            iconPainter = painterResource(R.drawable.theme_mode_system),
                            iconSize = ThemeScreenDefaults.iconSize,
                            title = stringResource(R.string.theme_mode_system),
                            trailing = { QrezzyRadioButton(selected = true, onClick = {}) },
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                    }
                }
            }
        }
    }
}

private object ThemeScreenDefaults {
    const val START_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp
}