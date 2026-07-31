package software.mazur.qrezzy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import software.mazur.qrezzy.core.common.crash.CrashReporter

@HiltAndroidApp
class QrezzyApplication : Application() {
    @Inject
    lateinit var crashReporter: CrashReporter

    override fun onCreate() {
        super.onCreate()
        crashReporter.setEnabled(!BuildConfig.DEBUG)
        crashReporter.log("QREZZY application started")
    }
}
