package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.BuildConfig
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun QrezzyAppVersion() {
    Text(
        text = "Version ${BuildConfig.VERSION_NAME}",
        color = TextPrimary,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .background(
                color = QrezzyPurple.copy(alpha = QrezzyAppVersionDefaults.BACKGROUND_ALPHA),
                shape = ShapeDefaults.ExtraLarge
            )
            .border(
                width = QrezzyAppVersionDefaults.borderWidth,
                color = QrezzyPurpleDark,
                shape = ShapeDefaults.ExtraLarge,
            )
            .padding(
                horizontal = QrezzyAppVersionDefaults.horizontalPadding,
                vertical = QrezzyAppVersionDefaults.verticalPadding,
            ),
    )
}

private object QrezzyAppVersionDefaults {
    const val BACKGROUND_ALPHA = 0.7f
    val borderWidth = 1.dp
    val horizontalPadding = 10.dp
    val verticalPadding = 3.dp
}