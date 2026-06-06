package software.mazur.qrezzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import software.mazur.qrezzy.core.designsystem.theme.QREZZYTheme
import software.mazur.qrezzy.core.navigation.QrezzyNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QREZZYTheme {
                QrezzyNavHost()
            }
        }
    }
}
