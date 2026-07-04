package software.mazur.qrezzy.domain.settings.model

enum class HistoryLimit(val value: Int?) {
    ITEMS_100(100),
    ITEMS_200(200),
    ITEMS_500(500),
    ITEMS_1000(1000),
    UNLIMITED(null)
}