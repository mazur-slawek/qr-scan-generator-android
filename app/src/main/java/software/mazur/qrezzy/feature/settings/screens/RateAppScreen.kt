package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyRateApp
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar

@Composable
fun RateAppScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = RateAppScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.rate_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = RateAppScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(RateAppScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_rate_app),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = RateAppScreenDefaults.imageTopPadding)
                            .height(RateAppScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyRateApp(title = stringResource(R.string.rate_screen_subtitle))
                }
            }
        }
    }
}

private object RateAppScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
}