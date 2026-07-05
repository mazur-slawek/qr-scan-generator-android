package software.mazur.qrezzy.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.mazur.qrezzy.feature.generator.GeneratorScreen
import software.mazur.qrezzy.feature.history.HistoryNavHost
import software.mazur.qrezzy.feature.home.HomeScreen
import software.mazur.qrezzy.feature.onboarding.OnboardingScreen
import software.mazur.qrezzy.feature.onboarding.OnboardingViewModel
import software.mazur.qrezzy.feature.scanner.ScannerScreen
import software.mazur.qrezzy.feature.settings.navigation.SettingsNavHost
import software.mazur.qrezzy.feature.splash.SplashScreen
import software.mazur.qrezzy.feature.splash.SplashViewModel

@Composable
fun QrezzyNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = QrezzyDestination.Splash.route) {
        composable(QrezzyDestination.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(
                onGetStartedClick = {
                    viewModel.checkStartDestination(
                        onOnboardingRequired = {
                            navController.navigate(QrezzyDestination.Onboarding.route) {
                                popUpTo(QrezzyDestination.Splash.route) { inclusive = true }
                            }
                        },
                        onHomeRequired = {
                            navController.navigate(QrezzyDestination.Home.route) {
                                popUpTo(QrezzyDestination.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }
            )
        }

        composable(QrezzyDestination.Onboarding.route) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onGetStartedClick = {
                    viewModel.completeOnboarding {
                        navController.navigate(QrezzyDestination.Home.route) {
                            popUpTo(QrezzyDestination.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(QrezzyDestination.Home.route) {
            HomeScreen()
        }

        composable(QrezzyDestination.Scanner.route) {
            ScannerScreen()
        }

        composable(QrezzyDestination.Generator.route) {
            GeneratorScreen()
        }

        composable(QrezzyDestination.History.route) {
            HistoryNavHost(onEmptyActionClick = {})
        }

        composable(QrezzyDestination.Settings.route) {
            SettingsNavHost()
        }
    }
}
