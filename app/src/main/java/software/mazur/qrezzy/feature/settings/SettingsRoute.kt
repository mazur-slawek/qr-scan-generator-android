package software.mazur.qrezzy.feature.settings

sealed class SettingsRoute(val route: String) {
    data object Settings : SettingsRoute("settings")
}
