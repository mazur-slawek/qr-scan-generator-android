package software.mazur.qrezzy.feature.settings

sealed class SettingsRoute(val route: String) {
    data object Settings : SettingsRoute("settings")
    data object Language : SettingsRoute("language")
    data object Theme : SettingsRoute("theme")
    data object Privacy : SettingsRoute("privacy")
    data object Permissions : SettingsRoute("permissions")
    data object MaximumHistoryItems : SettingsRoute("maximum_history_items")
    data object ClearAllHistory : SettingsRoute("clear_all_history")
    data object RateApp : SettingsRoute("rate_app")
    data object Contact : SettingsRoute("contact")
    data object OpenSourceLicense : SettingsRoute("open_source_license")
    data object Donate : SettingsRoute("donate")
}
