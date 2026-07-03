package software.mazur.qrezzy.feature.settings.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyRateApp
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun DonateScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(horizontal = DonateScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.donate_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = DonateScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(DonateScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_donate),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = DonateScreenDefaults.imageTopPadding)
                            .height(DonateScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyFieldWrapper(title = stringResource(R.string.donate_support_section_title)) {
                        Column {
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_coffee),
                                title = stringResource(R.string.donate_buy_coffee_title),
                                subtitle = stringResource(R.string.donate_buy_coffee_subtitle),
                                iconSize = DonateScreenDefaults.iconSize,
                                showDivider = false,
                                onClick = { openBuyMeACoffee(context) },
                            )
                        }
                    }
                }
                item {
                    QrezzyRateApp(title = stringResource(R.string.donate_other_support_title))
                }
            }
        }
    }
}

private fun openBuyMeACoffee(context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        DonateScreenDefaults.BUY_ME_A_COFFEE_URL.toUri(),
    )
    context.startActivity(intent)
}

private object DonateScreenDefaults {
    const val BUY_ME_A_COFFEE_URL = "https://buymeacoffee.com/slawek_mazur"
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}