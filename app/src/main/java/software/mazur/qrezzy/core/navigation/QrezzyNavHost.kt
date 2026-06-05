package software.mazur.qrezzy.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.feature.generator.GeneratorScreen
import software.mazur.qrezzy.feature.history.HistoryScreen
import software.mazur.qrezzy.feature.onboarding.OnboardingScreen
import software.mazur.qrezzy.feature.scanner.ScannerScreen
import software.mazur.qrezzy.feature.settings.SettingsScreen
import software.mazur.qrezzy.feature.splash.SplashScreen

@Composable
fun QrezzyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = QrezzyDestination.Splash.route,
    ) {
        composable(QrezzyDestination.Splash.route) {
            SplashScreen()
        }

        composable(QrezzyDestination.Onboarding.route) {
            OnboardingScreen()
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
