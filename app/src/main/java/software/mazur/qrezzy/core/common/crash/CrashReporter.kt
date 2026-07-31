package software.mazur.qrezzy.core.common.crash

interface CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun setEnabled(enabled: Boolean)
}
