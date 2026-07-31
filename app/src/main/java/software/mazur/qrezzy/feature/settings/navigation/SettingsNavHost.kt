package software.mazur.qrezzy.feature.settings.navigation

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.R
import software.mazur.qrezzy.feature.settings.SettingsScreen
import software.mazur.qrezzy.feature.settings.SettingsViewModel
import software.mazur.qrezzy.feature.settings.model.SettingsUiEvent
import software.mazur.qrezzy.feature.settings.screens.ClearAllHistoryScreen
import software.mazur.qrezzy.feature.settings.screens.ContactScreen
import software.mazur.qrezzy.feature.settings.screens.DonateScreen
import software.mazur.qrezzy.feature.settings.screens.LanguageScreen
import software.mazur.qrezzy.feature.settings.screens.MaxHistoryItemsScreen
import software.mazur.qrezzy.feature.settings.screens.OpenSourceLicenseScreen
import software.mazur.qrezzy.feature.settings.screens.PermissionsScreen
import software.mazur.qrezzy.feature.settings.screens.PrivacyScreen
import software.mazur.qrezzy.feature.settings.screens.RateAppScreen
import software.mazur.qrezzy.feature.settings.screens.ThemeScreen

@Composable
fun SettingsNavHost() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        settingsViewModel.events.collect { event ->
            when (event) {
                SettingsUiEvent.AutoSaveEnabled ->
                    Toast.makeText(context, R.string.settings_auto_save_enabled, Toast.LENGTH_SHORT).show()

                SettingsUiEvent.AutoSaveDisabled ->
                    Toast.makeText(context, R.string.settings_auto_save_disabled, Toast.LENGTH_SHORT).show()

                SettingsUiEvent.VibrationEnabled ->
                    Toast.makeText(context, R.string.settings_vibration_enabled, Toast.LENGTH_SHORT).show()

                SettingsUiEvent.VibrationDisabled ->
                    Toast.makeText(context, R.string.settings_vibration_disabled, Toast.LENGTH_SHORT).show()

                SettingsUiEvent.HistoryLimitChanged ->
                    Toast.makeText(context, R.string.history_limit_changed, Toast.LENGTH_SHORT).show()

                SettingsUiEvent.HistoryCleared ->
                    Toast.makeText(context, R.string.history_cleared, Toast.LENGTH_SHORT).show()
            }
        }
    }
    if (settingsUiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    } else {
        NavHost(
            navController = navController,
            startDestination = SettingsRoute.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable(SettingsRoute.Settings.route) {
                SettingsScreen(
                    uiState = settingsUiState,
                    onLanguageClick = { navController.navigate(SettingsRoute.Language.route) },
                    onThemeClick = { navController.navigate(SettingsRoute.Theme.route) },
                    onPrivacyClick = { navController.navigate(SettingsRoute.Privacy.route) },
                    onPermissionsClick = { navController.navigate(SettingsRoute.Permissions.route) },
                    onMaximumHistoryItemsClick = { navController.navigate(SettingsRoute.MaxHistoryItems.route) },
                    onClearAllHistoryClick = { navController.navigate(SettingsRoute.ClearAllHistory.route) },
                    onRateAppClick = { navController.navigate(SettingsRoute.RateApp.route) },
                    onContactClick = { navController.navigate(SettingsRoute.Contact.route) },
                    onOpenSourceLicensesClick = { navController.navigate(SettingsRoute.OpenSourceLicenses.route) },
                    onDonateClick = { navController.navigate(SettingsRoute.Donate.route) },
                    onAutoSaveScansChanged = settingsViewModel::onAutoSaveScansChanged,
                    onVibrationEnabledChanged = settingsViewModel::onVibrationEnabledChanged
                )
            }
            composable(SettingsRoute.Language.route) {
                LanguageScreen(
                    selectedLanguage = settingsUiState.language,
                    onLanguageSelected = settingsViewModel::onLanguageSelected,
                    onBackClick = navController::popBackStack
                )
            }

            composable(SettingsRoute.Theme.route) {
                ThemeScreen(
                    selectedTheme = settingsUiState.theme,
                    onThemeSelected = settingsViewModel::onThemeSelected,
                    onBackClick = navController::popBackStack
                )
            }

            composable(SettingsRoute.Privacy.route) {
                PrivacyScreen(navController::popBackStack)
            }

            composable(SettingsRoute.Permissions.route) {
                PermissionsScreen(navController::popBackStack)
            }

            composable(SettingsRoute.MaxHistoryItems.route) {
                LaunchedEffect(Unit) {
                    settingsViewModel.refreshHistoryLimitState()
                }
                MaxHistoryItemsScreen(
                    settingsUiState = settingsUiState,
                    selectedLimit = settingsUiState.historyLimit,
                    onHistoryLimitSelected = settingsViewModel::onHistoryLimitSelected,
                    onBackClick = navController::popBackStack
                )
            }

            composable(SettingsRoute.ClearAllHistory.route) {
                LaunchedEffect(Unit) {
                    settingsViewModel.refreshHistorySummary()
                }
                ClearAllHistoryScreen(
                    settingsUiState = settingsUiState,
                    onBackClick = navController::popBackStack,
                    itemsCount = settingsUiState.historyItemsCount,
                    latestCreatedAt = settingsUiState.latestHistoryItemCreatedAt,
                    onClearAllHistoryClick = settingsViewModel::onClearHistoryClick,
                    onClearHistoryConfirmed = settingsViewModel::onClearHistoryConfirmed,
                    onClearHistoryDialogDismissed = settingsViewModel::onClearHistoryDialogDismissed
                )
            }

            composable(SettingsRoute.RateApp.route) {
                RateAppScreen(navController::popBackStack)
            }

            composable(SettingsRoute.Contact.route) {
                ContactScreen(navController::popBackStack)
            }

            composable(SettingsRoute.OpenSourceLicenses.route) {
                OpenSourceLicenseScreen(navController::popBackStack)
            }

            composable(SettingsRoute.Donate.route) {
                DonateScreen(navController::popBackStack)
            }
        }
    }
}
