package software.mazur.qrezzy.navigation

sealed class QrezzyDestination(val route: String) {
    data object Splash : QrezzyDestination("splash")
    data object Home : QrezzyDestination("home")
    data object Onboarding : QrezzyDestination("onboarding")
    data object Scanner : QrezzyDestination("scanner")
    data object Generator : QrezzyDestination("generator")
    data object History : QrezzyDestination("history")
    data object Settings : QrezzyDestination("settings")
}