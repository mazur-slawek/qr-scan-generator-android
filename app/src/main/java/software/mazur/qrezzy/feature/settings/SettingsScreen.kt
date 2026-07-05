package software.mazur.qrezzy.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyCopyright
import software.mazur.qrezzy.core.designsystem.components.QrezzyListItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzySwitch
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.SettingsListFooterCard
import software.mazur.qrezzy.feature.settings.components.SettingsListHeaderCard
import software.mazur.qrezzy.feature.settings.mapper.displayName
import software.mazur.qrezzy.feature.settings.model.SettingsUiState

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onPermissionsClick: () -> Unit,
    onMaximumHistoryItemsClick: () -> Unit,
    onClearAllHistoryClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onContactClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit,
    onDonateClick: () -> Unit,
    onAutoSaveScansChanged: (Boolean) -> Unit,
    onVibrationEnabledChanged: (Boolean) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = SettingsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.navigation_title_settings,
            subtitleResId = R.string.navigation_subtitle_settings
        ) {
            QrezzyTopBarButton(onClick = onDonateClick, iconPainter = painterResource(R.drawable.qrezzy_donate))
        }

        LazyColumn(
            modifier = Modifier.weight(SettingsScreenDefaults.LIST_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(SettingsScreenDefaults.sectionSpacing)
        ) {
            item { SettingsListHeaderCard(modifier = Modifier.padding(top = SettingsScreenDefaults.topPadding)) }

            item {
                QrezzyListSection(title = stringResource(R.string.settings_section_general)) {
                    Column {
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_language),
                            title = stringResource(R.string.settings_language),
                            value = uiState.language.displayName(),
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyYellowDark,
                            onClick = onLanguageClick
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_theme),
                            title = stringResource(R.string.settings_theme),
                            value = uiState.theme.displayName(),
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyMintDark,
                            onClick = onThemeClick,
                        )
                    }
                }
            }

            item {
                QrezzyListSection(title = stringResource(R.string.settings_section_scanner)) {
                    Column {
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_autosave),
                            title = stringResource(R.string.settings_auto_save_scanned_qr),
                            trailing = {
                                QrezzySwitch(
                                    checked = uiState.autoSaveScans,
                                    onCheckedChange = onAutoSaveScansChanged
                                )
                            },
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_vibro),
                            title = stringResource(R.string.settings_vibrate_on_scan),
                            trailing = {
                                QrezzySwitch(
                                    checked = uiState.vibrationEnabled,
                                    onCheckedChange = onVibrationEnabledChanged
                                )
                            },
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyPurpleDark,
                        )
                    }
                }
            }

            item {
                QrezzyListSection(title = stringResource(R.string.settings_section_history)) {
                    Column {
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_history_limit),
                            title = stringResource(R.string.settings_max_history_items),
                            value = uiState.historyLimit.displayName(),
                            onClick = onMaximumHistoryItemsClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyYellowDark,
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_delete),
                            title = stringResource(R.string.settings_clear_all_history),
                            titleColor = QrezzyPinkDark,
                            showDivider = false,
                            onClick = onClearAllHistoryClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyPinkDark,
                        )
                    }
                }
            }

            item {
                QrezzyListSection(title = stringResource(R.string.settings_section_support)) {
                    Column {
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_privacy),
                            title = stringResource(R.string.settings_privacy),
                            onClick = onPrivacyClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyMintDark
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_permission),
                            title = stringResource(R.string.settings_permission),
                            onClick = onPermissionsClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyPurpleDark
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_rate),
                            title = stringResource(R.string.settings_rate_app),
                            onClick = onRateAppClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyYellowDark
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_contact),
                            title = stringResource(R.string.settings_contact),
                            onClick = onContactClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyMintDark
                        )
                        QrezzyListItem(
                            iconPainter = painterResource(R.drawable.qrezzy_open_source_licenses),
                            title = stringResource(R.string.settings_open_source_licenses),
                            onClick = onOpenSourceLicensesClick,
                            iconSize = SettingsScreenDefaults.iconSize,
                            iconBackgroundColor = QrezzyPurpleDark,
                            showDivider = false
                        )
                    }
                }
            }
            item {
                SettingsListFooterCard(
                    onClick = onDonateClick,
                    modifier = Modifier.padding(bottom = SettingsScreenDefaults.bottomPadding)
                )
                QrezzyCopyright(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SettingsScreenDefaults.bottomPadding))
            }
        }
    }
}

private object SettingsScreenDefaults {
    val horizontalPadding = 16.dp
    val sectionSpacing = 14.dp
    val topPadding = 16.dp
    val bottomPadding = 16.dp
    const val LIST_WEIGHT = 1f
    val iconSize = 40.dp
    val imageHeight = 90.dp
}
