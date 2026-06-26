package software.mazur.qrezzy.feature.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.feature.settings.screens.AboutAppScreen
import software.mazur.qrezzy.feature.settings.screens.ClearAllHistoryScreen
import software.mazur.qrezzy.feature.settings.screens.DonateScreen
import software.mazur.qrezzy.feature.settings.screens.HelpSupportScreen
import software.mazur.qrezzy.feature.settings.screens.LanguageScreen
import software.mazur.qrezzy.feature.settings.screens.MaximumHistoryItemsScreen
import software.mazur.qrezzy.feature.settings.screens.OpenSourceLicenseScreen
import software.mazur.qrezzy.feature.settings.screens.PermissionsScreen
import software.mazur.qrezzy.feature.settings.screens.PrivacyScreen
import software.mazur.qrezzy.feature.settings.screens.RateAppScreen
import software.mazur.qrezzy.feature.settings.screens.ThemeScreen

@Composable
fun SettingsNavHost() {
    val navController = rememberNavController()

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
                onLanguageClick = { navController.navigate(SettingsRoute.Language.route) },
                onThemeClick = { navController.navigate(SettingsRoute.Theme.route) },
                onPrivacyClick = { navController.navigate(SettingsRoute.Privacy.route) },
                onPermissionsClick = { navController.navigate(SettingsRoute.Permissions.route) },
                onMaximumHistoryItemsClick = { navController.navigate(SettingsRoute.MaximumHistoryItems.route) },
                onClearAllHistoryClick = { navController.navigate(SettingsRoute.ClearAllHistory.route) },
                onAboutAppClick = { navController.navigate(SettingsRoute.AboutApp.route) },
                onRateAppClick = { navController.navigate(SettingsRoute.RateApp.route) },
                onHelpSupportClick = { navController.navigate(SettingsRoute.HelpSupport.route) },
                onOpenSourceLicensesClick = { navController.navigate(SettingsRoute.OpenSourceLicense.route) },
                onDonateClick = { navController.navigate(SettingsRoute.Donate.route) },
            )
        }
        composable(SettingsRoute.Language.route) {
            LanguageScreen(navController::popBackStack)
        }

        composable(SettingsRoute.Theme.route) {
            ThemeScreen(navController::popBackStack)
        }

        composable(SettingsRoute.Privacy.route) {
            PrivacyScreen(navController::popBackStack)
        }

        composable(SettingsRoute.Permissions.route) {
            PermissionsScreen(navController::popBackStack)
        }

        composable(SettingsRoute.MaximumHistoryItems.route) {
            MaximumHistoryItemsScreen(navController::popBackStack)
        }

        composable(SettingsRoute.ClearAllHistory.route) {
            ClearAllHistoryScreen(navController::popBackStack)
        }

        composable(SettingsRoute.AboutApp.route) {
            AboutAppScreen(navController::popBackStack)
        }

        composable(SettingsRoute.RateApp.route) {
            RateAppScreen(navController::popBackStack)
        }

        composable(SettingsRoute.HelpSupport.route) {
            HelpSupportScreen(navController::popBackStack)
        }

        composable(SettingsRoute.OpenSourceLicense.route) {
            OpenSourceLicenseScreen(navController::popBackStack)
        }

        composable(SettingsRoute.Donate.route) {
            DonateScreen(navController::popBackStack)
        }
    }
}
