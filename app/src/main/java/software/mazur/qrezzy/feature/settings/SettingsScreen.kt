package software.mazur.qrezzy.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzySwitch
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.feature.settings.components.SettingsItem
import software.mazur.qrezzy.feature.settings.components.SettingsListFooter
import software.mazur.qrezzy.feature.settings.components.SettingsListHeader

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(horizontal = SettingsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_settings,
            subtitleResId = R.string.navigation_subtitle_settings
        ) {
            QrezzyTopBarButton(
                onClick = {},
                enabled = false,
                icon = Icons.Default.Favorite,
                iconTint = QrezzyPurpleDark
            )
        }

        LazyColumn(
            modifier = Modifier.weight(SettingsScreenDefaults.LIST_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(SettingsScreenDefaults.sectionSpacing)
        ) {
            item { SettingsListHeader(modifier = Modifier.padding(top = SettingsScreenDefaults.topPadding)) }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.settings_section_general)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Language,
                            iconTintColor = QrezzyPurpleDark,
                            iconBackgroundColor = QrezzyPurpleDark,
                            title = stringResource(R.string.settings_language),
                            value = stringResource(R.string.settings_language_polish),
                            onClick = {}
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Palette,
                            title = stringResource(R.string.settings_theme),
                            value = stringResource(R.string.settings_theme_system),
                            onClick = {},
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.PrivacyTip,
                            title = stringResource(R.string.settings_privacy),
                            onClick = {},
                            iconTintColor = QrezzyPinkDark,
                            iconBackgroundColor = QrezzyPinkDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Checklist,
                            title = stringResource(R.string.settings_permission),
                            showDivider = false,
                            onClick = {},
                            iconTintColor = TextSecondary,
                            iconBackgroundColor = QrezzyYellowDark,
                        )
                    }
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.settings_section_scanner)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Save,
                            title = stringResource(R.string.settings_auto_save_scanned_qr),
                            trailing = { QrezzySwitch(checked = true, onCheckedChange = {}) },
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Vibration,
                            title = stringResource(R.string.settings_vibrate_on_scan),
                            trailing = { QrezzySwitch(checked = true, onCheckedChange = {}) },
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Link,
                            title = stringResource(R.string.settings_open_links_automatically),
                            showDivider = false,
                            trailing = { QrezzySwitch(checked = false, onCheckedChange = {}) },
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                    }
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.settings_section_history)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.History,
                            title = stringResource(R.string.settings_max_history_items),
                            value = "500",
                            onClick = {},
                            iconTintColor = TextSecondary,
                            iconBackgroundColor = QrezzyYellowDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.DeleteOutline,
                            title = stringResource(R.string.settings_clear_all_history),
                            titleColor = QrezzyPinkDark,
                            showDivider = false,
                            onClick = {},
                            iconTintColor = QrezzyPinkDark,
                            iconBackgroundColor = QrezzyPinkDark,
                        )
                    }
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.settings_section_support)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Info,
                            title = stringResource(R.string.settings_about_app),
                            onClick = {},
                            iconTintColor = QrezzyPurpleDark,
                            iconBackgroundColor = QrezzyPurpleDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.StarBorder,
                            title = stringResource(R.string.settings_rate_app),
                            onClick = {},
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = stringResource(R.string.settings_help_support),
                            onClick = {},
                            iconTintColor = QrezzyPinkDark,
                            iconBackgroundColor = QrezzyPinkDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.Description,
                            title = stringResource(R.string.settings_open_source_licenses),
                            showDivider = false,
                            onClick = {},
                            iconTintColor = TextSecondary,
                            iconBackgroundColor = QrezzyYellowDark,
                        )
                    }
                }
            }
            item { SettingsListFooter(modifier = Modifier.padding(bottom = SettingsScreenDefaults.bottomPadding)) }
        }
    }
}

private object SettingsScreenDefaults {
    val horizontalPadding = 16.dp
    val sectionSpacing = 14.dp
    val topPadding = 16.dp
    val bottomPadding = 16.dp
    const val LIST_WEIGHT = 1f

}
