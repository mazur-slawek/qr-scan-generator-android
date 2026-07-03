package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyAppVersion
import software.mazur.qrezzy.core.designsystem.components.QrezzyCopyright
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun AboutAppScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = AboutAppScreenDefaults.padding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_about_screen_title,
            subtitleResId = R.string.settings_about_screen_subtitle,
            onBackClick = onBackClick,
        )
        Column(modifier = Modifier.fillMaxHeight()) {
            QrezzyAnimatedStars(
                starsCount = AboutAppScreenDefaults.STAR_COUNT,
                modifier = Modifier.weight(AboutAppScreenDefaults.CONTENT_WEIGHT)
            ) {
                Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(AboutAppScreenDefaults.topSpacing))
                    Image(
                        painter = painterResource(R.drawable.qrezzy),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                    )
                    Spacer(modifier = Modifier.height(AboutAppScreenDefaults.logoSpacing))
                    Image(
                        painter = painterResource(R.drawable.qrezzy_slogan),
                        contentDescription = null,
                        modifier = Modifier.height(AboutAppScreenDefaults.sloganHeight),
                    )
                    Spacer(modifier = Modifier.height(AboutAppScreenDefaults.sectionSpacing))
                    QrezzyAppVersion()
                    Spacer(modifier = Modifier.height(AboutAppScreenDefaults.sectionSpacing))
                    Text(
                        text = stringResource(R.string.about_app_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            QrezzyCopyright(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(AboutAppScreenDefaults.padding))
        }
    }
}

private object AboutAppScreenDefaults {
    const val STAR_COUNT = 250
    const val CONTENT_WEIGHT = 1f
    val padding = 16.dp
    val topSpacing = 32.dp
    val logoSpacing = 12.dp
    val sectionSpacing = 16.dp
    val sloganHeight = 17.dp
}