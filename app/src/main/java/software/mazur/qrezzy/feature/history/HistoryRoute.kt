package software.mazur.qrezzy.feature.history

sealed class HistoryRoute(val route: String) {
    data object List : HistoryRoute("history_list")

    data object Details : HistoryRoute("history_details/{historyId}") {
        const val HISTORY_ID_ARG = "historyId"

        fun createRoute(historyId: Long): String {
            return "history_details/$historyId"
        }
    }
}