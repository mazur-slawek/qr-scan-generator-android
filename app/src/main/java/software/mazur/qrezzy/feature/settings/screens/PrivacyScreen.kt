package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Share
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
        QrezzyTopBar(
            titleResId = R.string.settings_privacy_screen_title,
            subtitleResId = R.string.settings_privacy_screen_subtitle,
            onBackClick = onBackClick,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = PrivacyScreenDefaults.STAR_COUNT,
                    modifier = Modifier.height(PrivacyScreenDefaults.imageHeight),
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_privacy),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(PrivacyScreenDefaults.imagePadding)
                            .fillMaxHeight()
                    )
                }
            }

            item {
                QrezzyFieldWrapper {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Security,
                            iconSize = PrivacyScreenDefaults.iconSize,
                            title = stringResource(R.string.privacy_data_collect_title),
                            subtitle = stringResource(R.string.privacy_data_collect_subtitle),
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Description,
                            iconSize = PrivacyScreenDefaults.iconSize,
                            title = stringResource(R.string.privacy_data_usage_title),
                            subtitle = stringResource(R.string.privacy_data_usage_subtitle),
                            iconTintColor = QrezzyYellowDark,
                            iconBackgroundColor = QrezzyYellowDark
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Share,
                            iconSize = PrivacyScreenDefaults.iconSize,
                            title = stringResource(R.string.privacy_data_sharing_title),
                            subtitle = stringResource(R.string.privacy_data_sharing_subtitle),
                            iconTintColor = QrezzyPinkDark,
                            iconBackgroundColor = QrezzyPinkDark
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Lock,
                            iconSize = PrivacyScreenDefaults.iconSize,
                            title = stringResource(R.string.privacy_data_security_title),
                            subtitle = stringResource(R.string.privacy_data_security_subtitle),
                            iconTintColor = QrezzyPurpleDark,
                            iconBackgroundColor = QrezzyPurpleDark
                        )
                        SettingsItem(
                            icon = Icons.Outlined.DeleteOutline,
                            iconSize = PrivacyScreenDefaults.iconSize,
                            title = stringResource(R.string.privacy_data_deletion_title),
                            subtitle = stringResource(R.string.privacy_data_deletion_subtitle),
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark
                        )
                    }
                }
            }
        }
    }
}

private object PrivacyScreenDefaults {
    const val STAR_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp
}