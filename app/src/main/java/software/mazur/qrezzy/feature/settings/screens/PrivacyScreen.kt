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
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun PrivacyScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = PrivacyScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.privacy_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = PrivacyScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(PrivacyScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_privacy),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = PrivacyScreenDefaults.imageTopPadding)
                            .height(PrivacyScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyFieldWrapper(title = stringResource(R.string.privacy_screen_subtitle)) {
                        Column {
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_privacy),
                                iconSize = PrivacyScreenDefaults.iconSize,
                                title = stringResource(R.string.privacy_data_collect_title),
                                subtitle = stringResource(R.string.privacy_data_collect_subtitle),
                                iconBackgroundColor = QrezzyMintDark
                            )
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_list),
                                iconSize = PrivacyScreenDefaults.iconSize,
                                title = stringResource(R.string.privacy_data_usage_title),
                                subtitle = stringResource(R.string.privacy_data_usage_subtitle),
                                iconBackgroundColor = QrezzyPurpleDark
                            )
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_share),
                                iconSize = PrivacyScreenDefaults.iconSize,
                                title = stringResource(R.string.privacy_data_sharing_title),
                                subtitle = stringResource(R.string.privacy_data_sharing_subtitle),
                                iconBackgroundColor = QrezzyMintDark
                            )
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_lock),
                                iconSize = PrivacyScreenDefaults.iconSize,
                                title = stringResource(R.string.privacy_data_security_title),
                                subtitle = stringResource(R.string.privacy_data_security_subtitle),
                                iconBackgroundColor = QrezzyYellowDark
                            )
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_delete),
                                iconSize = PrivacyScreenDefaults.iconSize,
                                title = stringResource(R.string.privacy_data_deletion_title),
                                subtitle = stringResource(R.string.privacy_data_deletion_subtitle),
                                iconBackgroundColor = QrezzyPinkDark
                            )
                        }
                    }
                }
            }
        }
    }
}

private object PrivacyScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}