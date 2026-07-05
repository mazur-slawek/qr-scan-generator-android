package software.mazur.qrezzy.core.common.vibration

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidVibrationService @Inject constructor(@ApplicationContext private val context: Context) : VibrationService {
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrateShort() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context
                .getSystemService(VibratorManager::class.java)
                .defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
        }

        if (!vibrator.hasVibrator()) return
        val effect = VibrationEffect.createOneShot(
            VIBRATION_DURATION_MILLIS,
            VibrationEffect.DEFAULT_AMPLITUDE,
        )
        vibrator.vibrate(effect)
    }

    private companion object {
        const val VIBRATION_DURATION_MILLIS = 80L
    }
}