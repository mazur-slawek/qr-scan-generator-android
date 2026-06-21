package software.mazur.qrezzy.feature.history

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import software.mazur.qrezzy.feature.history.components.HistoryEmptyAction
import software.mazur.qrezzy.feature.history.details.HistoryDetailsScreen

@Composable
fun HistoryNavHost(onEmptyActionClick: (HistoryEmptyAction) -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HistoryRoute.List.route,
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
        composable(HistoryRoute.List.route) {
            HistoryScreen(
                onEmptyActionClick = onEmptyActionClick,
                onHistoryItemClick = { historyId ->
                    navController.navigate(HistoryRoute.Details.createRoute(historyId))
                },
            )
        }

        composable(
            route = HistoryRoute.Details.route,
            arguments =
                listOf(
                    navArgument(HistoryRoute.Details.HISTORY_ID_ARG) { type = NavType.LongType },
                ),
        ) {
            HistoryDetailsScreen(onBackClick = navController::popBackStack)
        }
    }
}
