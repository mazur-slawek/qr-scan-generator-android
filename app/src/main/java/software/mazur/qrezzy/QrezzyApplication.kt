package software.mazur.qrezzy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import software.mazur.qrezzy.core.common.crash.CrashReporter
import javax.inject.Inject

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