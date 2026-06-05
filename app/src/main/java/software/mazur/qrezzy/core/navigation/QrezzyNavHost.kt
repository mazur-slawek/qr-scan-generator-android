package software.mazur.qrezzy.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.feature.generator.GeneratorScreen
import software.mazur.qrezzy.feature.history.HistoryScreen
import software.mazur.qrezzy.feature.home.HomeScreen
import software.mazur.qrezzy.feature.onboarding.OnboardingScreen
import software.mazur.qrezzy.feature.scanner.ScannerScreen
import software.mazur.qrezzy.feature.settings.SettingsScreen
import software.mazur.qrezzy.feature.splash.SplashScreen

object QrezzyDestinations {
    const val SPLASH = "splash"
    const val HOME = "home"
}

@Composable
fun QrezzyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = QrezzyDestination.Splash.route,
    ) {
        composable(QrezzyDestinations.SPLASH) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(QrezzyDestinations.HOME) {
                        popUpTo(QrezzyDestinations.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(QrezzyDestination.Onboarding.route) {
            OnboardingScreen()
        }

        composable(QrezzyDestinations.HOME) {
            HomeScreen()
        }

        composable(QrezzyDestination.Scanner.route) {
            ScannerScreen()
        }

        composable(QrezzyDestination.Generator.route) {
            GeneratorScreen()
        }

        composable(QrezzyDestination.History.route) {
            HistoryScreen()
        }

        composable(QrezzyDestination.Settings.route) {
            SettingsScreen()
        }
    }
}
