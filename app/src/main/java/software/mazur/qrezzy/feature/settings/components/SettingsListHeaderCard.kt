package software.mazur.qrezzy.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyAppVersion
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.theme.LocalIsDarkTheme

@Composable
fun SettingsListHeaderCard(modifier: Modifier = Modifier) {
    val isDarkTheme = LocalIsDarkTheme.current
    QrezzyListSection(modifier = modifier.wrapContentSize()) {
        QrezzyAnimatedStars(
            starsCount = SettingsListHeaderDefaults.STARTS_COUNT,
            modifier = Modifier.height(SettingsListHeaderDefaults.headerHeight)
        ) {
            Row(
                modifier = Modifier.padding(all = SettingsListHeaderDefaults.padding),
                horizontalArrangement = Arrangement.spacedBy(SettingsListHeaderDefaults.contentSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(SettingsListHeaderDefaults.LOGO_WEIGHT),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.qrezzy),
                            contentDescription = null,
                            modifier = Modifier.height(SettingsListHeaderDefaults.logoHeight)
                        )
                        Image(
                            painter = painterResource(
                                if (isDarkTheme) R.drawable.qrezzy_slogan_dark else R.drawable.qrezzy_slogan
                            ),
                            modifier = Modifier
                                .padding(
                                    top = SettingsListHeaderDefaults.sloganTopSpacing,
                                    start = SettingsListHeaderDefaults.sloganStartPadding
                                )
                                .height(SettingsListHeaderDefaults.sloganHeight),
                            contentDescription = null
                        )
                    }
                    QrezzyAppVersion(modifier = Modifier.padding(start = SettingsListHeaderDefaults.sloganStartPadding))
                }
                Image(
                    painter = painterResource(R.drawable.qrezzy_mascot_settings),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

private object SettingsListHeaderDefaults {
    const val STARTS_COUNT = 80
    const val LOGO_WEIGHT = 1f
    val padding = 16.dp
    val headerHeight = 150.dp
    val logoHeight = 47.dp
    val sloganHeight = 13.dp
    val contentSpacing = 16.dp
    val sloganTopSpacing = 13.dp
    val sloganStartPadding = 4.dp
}
