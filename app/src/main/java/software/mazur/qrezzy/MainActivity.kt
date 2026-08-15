package software.mazur.qrezzy

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import software.mazur.qrezzy.core.designsystem.theme.QREZZYTheme
import software.mazur.qrezzy.core.designsystem.theme.resolveIsDarkTheme
import software.mazur.qrezzy.domain.settings.model.AppSettings
import software.mazur.qrezzy.domain.settings.usecase.InitializeAppSettingsUseCase
import software.mazur.qrezzy.domain.settings.usecase.ObserveAppSettingsUseCase
import software.mazur.qrezzy.navigation.QrezzyNavHost

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var initializeAppSettingsUseCase: InitializeAppSettingsUseCase

    @Inject
    lateinit var observeAppSettingsUseCase: ObserveAppSettingsUseCase

    private fun setupSystemBars() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemBars()

        lifecycleScope.launch {
            initializeAppSettingsUseCase()
        }

        setContent {
            val appSettings by observeAppSettingsUseCase().collectAsStateWithLifecycle(initialValue = AppSettings())
            val isDarkTheme = resolveIsDarkTheme(appSettings.theme)
            QREZZYTheme(isDarkTheme = isDarkTheme) {
                QrezzyNavHost()
            }
        }
    }
}
