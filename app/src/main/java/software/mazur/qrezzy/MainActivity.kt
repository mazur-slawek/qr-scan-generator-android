package software.mazur.qrezzy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.designsystem.theme.QREZZYTheme
import software.mazur.qrezzy.core.navigation.QrezzyNavHost
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.usecase.InitializeAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var initializeAppSettingsUseCase: InitializeAppSettingsUseCase

    @Inject
    lateinit var observeAppSettingsUseCase: ObserveAppSettingsUseCase

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
//        WindowCompat.getInsetsController(window, window.decorView).apply {
//            isAppearanceLightStatusBars = true
//            isAppearanceLightNavigationBars = true
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemBars()

        lifecycleScope.launch {
            initializeAppSettingsUseCase()
        }

        setContent {
            val appSettings by observeAppSettingsUseCase().collectAsState(initial = AppSettings())
            QREZZYTheme(appTheme = appSettings.theme) {
                QrezzyNavHost()
            }
        }
    }
}