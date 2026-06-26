package software.mazur.qrezzy.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyAppVersion

@Composable
fun SettingsListHeader(modifier: Modifier = Modifier) {
    QrezzyAnimatedStars(
        starsCount = SettingsListHeaderDefaults.STARTS_COUNT,
        modifier = modifier.height(SettingsListHeaderDefaults.headerHeight),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SettingsListHeaderDefaults.contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(SettingsListHeaderDefaults.LOGO_WEIGHT),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Image(
                    painter = painterResource(R.drawable.qrezzy),
                    contentDescription = null,
                    modifier = Modifier.height(SettingsListHeaderDefaults.logoHeight),
                )
                Column(modifier = Modifier.padding(start = SettingsListHeaderDefaults.sloganStartPadding)) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_slogan),
                        modifier = Modifier.height(SettingsListHeaderDefaults.sloganHeight),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(SettingsListHeaderDefaults.versionSpacing))
                    QrezzyAppVersion()
                }
            }
            Image(
                painter = painterResource(R.drawable.qrezzy_mascot_settings),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}

private object SettingsListHeaderDefaults {
    const val STARTS_COUNT = 50
    const val LOGO_WEIGHT = 1f
    val headerHeight = 125.dp
    val logoHeight = 47.dp
    val sloganHeight = 13.dp
    val contentSpacing = 16.dp
    val versionSpacing = 16.dp
    val sloganStartPadding = 4.dp
}