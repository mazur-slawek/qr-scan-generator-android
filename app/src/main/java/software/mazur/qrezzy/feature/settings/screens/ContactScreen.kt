package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar

@Composable
fun ContactScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(HelpSupportScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_contact_screen_title,
            subtitleResId = R.string.settings_contact_screen_subtitle,
            onBackClick = onBackClick
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = HelpSupportScreenDefaults.START_COUNT,
                    modifier = Modifier.height(HelpSupportScreenDefaults.imageHeight)
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_help_support),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(HelpSupportScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

private object HelpSupportScreenDefaults {
    const val START_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
}