package software.mazur.qrezzy

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.designsystem.theme.QREZZYTheme
import software.mazur.qrezzy.core.navigation.QrezzyNavHost
import software.mazur.qrezzy.domain.settings.usecase.InitializeAppSettingsUseCase
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var initializeAppSettingsUseCase: InitializeAppSettingsUseCase

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isAppearanceLightNavigationBars = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemBars()

        lifecycleScope.launch {
            initializeAppSettingsUseCase()
        }

        setContent {
            QREZZYTheme {
                QrezzyNavHost()
            }
        }
    }
}