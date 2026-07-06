package software.mazur.qrezzy.test

import software.mazur.qrezzy.core.common.crash.CrashReporter

class FakeCrashReporter : CrashReporter {
    val logs = mutableListOf<String>()
    val exceptions = mutableListOf<Throwable>()
    var enabled: Boolean? = null

    override fun log(message: String) {
        logs.add(message)
    }

    override fun recordException(throwable: Throwable) {
        exceptions.add(throwable)
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}