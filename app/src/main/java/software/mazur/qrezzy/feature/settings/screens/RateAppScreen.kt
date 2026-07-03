package software.mazur.qrezzy.feature.settings.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
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
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun RateAppScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val shareText = stringResource(R.string.rate_app_share_text)
    val feedbackSubject = stringResource(R.string.rate_app_feedback_subject)

    Column(modifier = Modifier.padding(horizontal = RateAppScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_rate_screen_title,
            subtitleResId = R.string.settings_rate_screen_subtitle,
            onBackClick = onBackClick,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(RateAppScreenDefaults.sectionSpacing),
        ) {
            item {
                QrezzyAnimatedStars(
                    starsCount = RateAppScreenDefaults.STAR_COUNT,
                    modifier = Modifier.height(RateAppScreenDefaults.imageHeight),
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_rate_app),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(RateAppScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.rate_app_section_store)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.StarOutline,
                            iconSize = RateAppScreenDefaults.iconSize,
                            title = stringResource(R.string.rate_app_store_title),
                            subtitle = stringResource(R.string.rate_app_store_subtitle),
                            iconTintColor = QrezzyYellowDark,
                            iconBackgroundColor = QrezzyYellowDark,
                            showDivider = false,
                            onClick = { openPlayStore(context) },
                        )
                    }
                }
            }

            item {
                QrezzyFieldWrapper(title = stringResource(R.string.rate_app_section_support)) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Share,
                            iconSize = RateAppScreenDefaults.iconSize,
                            title = stringResource(R.string.rate_app_share_title),
                            subtitle = stringResource(R.string.rate_app_share_subtitle),
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                            onClick = { shareApp(context = context, text = shareText) },
                        )

                        SettingsItem(
                            icon = Icons.Outlined.MailOutline,
                            iconSize = RateAppScreenDefaults.iconSize,
                            title = stringResource(R.string.rate_app_feedback_title),
                            subtitle = stringResource(R.string.rate_app_feedback_subtitle),
                            iconTintColor = QrezzyPurpleDark,
                            iconBackgroundColor = QrezzyPurpleDark,
                            showDivider = false,
                            onClick = { sendFeedbackEmail(context = context, subject = feedbackSubject) }
                        )
                    }
                }
            }
        }
    }
}

private fun openPlayStore(context: Context) {
    val packageName = context.packageName
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        "market://details?id=$packageName".toUri(),
    )
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        "https://play.google.com/store/apps/details?id=$packageName".toUri(),
    )
    runCatching {
        context.startActivity(marketIntent)
    }.onFailure {
        context.startActivity(browserIntent)
    }
}

private fun shareApp(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val title = context.getString(R.string.rate_app_share_chooser_title)
    context.startActivity(Intent.createChooser(intent, title))
}

private fun sendFeedbackEmail(context: Context, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:${RateAppScreenDefaults.EMAIL_ADDRESS}".toUri()
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
}

private object RateAppScreenDefaults {
    const val EMAIL_ADDRESS = "slawek.mazur.software@gmail.com"
    const val STAR_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val sectionSpacing = 16.dp
    val iconSize = 40.dp
}