package software.mazur.qrezzy.feature.settings.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.feature.settings.SettingsScreen
import software.mazur.qrezzy.feature.settings.SettingsViewModel
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
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = SettingsRoute.Settings.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300),
            )
        },
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
                onVibrationEnabledChanged = settingsViewModel::onVibrationEnabledChanged,
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
            MaxHistoryItemsScreen(
                selectedLimit = settingsUiState.historyLimit,
                onHistoryLimitSelected = settingsViewModel::onHistoryLimitSelected,
                onBackClick = navController::popBackStack
            )
        }

        composable(SettingsRoute.ClearAllHistory.route) {
            ClearAllHistoryScreen(navController::popBackStack)
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
