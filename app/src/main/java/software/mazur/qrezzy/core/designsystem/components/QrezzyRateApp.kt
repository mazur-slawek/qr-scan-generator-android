package software.mazur.qrezzy.core.designsystem.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark

@Composable
fun QrezzyRateApp(title: String) {
    val context = LocalContext.current
    val shareText = stringResource(R.string.rate_app_share_text)
    val feedbackSubject = stringResource(R.string.rate_app_feedback_subject)
    QrezzyListSection(title = title) {
        Column {
            QrezzyListItem(
                iconPainter = painterResource(R.drawable.qrezzy_rate),
                title = stringResource(R.string.rate_app_title),
                subtitle = stringResource(R.string.rate_app_subtitle),
                iconBackgroundColor = QrezzyYellowDark,
                iconSize = QrezzyRateAppDefaults.IconSize,
                onClick = { launchPlayStore(context) }
            )
            QrezzyListItem(
                iconPainter = painterResource(R.drawable.qrezzy_share),
                title = stringResource(R.string.rate_app_share_title),
                subtitle = stringResource(R.string.rate_app_share_subtitle),
                iconBackgroundColor = QrezzyMintDark,
                iconSize = QrezzyRateAppDefaults.IconSize,
                onClick = { shareApplication(context = context, text = shareText) }
            )
            QrezzyListItem(
                iconPainter = painterResource(R.drawable.qrezzy_email),
                title = stringResource(R.string.rate_app_feedback_title),
                subtitle = stringResource(R.string.rate_app_feedback_subtitle),
                iconBackgroundColor = QrezzyPurpleDark,
                iconSize = QrezzyRateAppDefaults.IconSize,
                onClick = { sendFeedbackEmail(context = context, subject = feedbackSubject) },
                showDivider = false
            )
        }
    }
}

private fun launchPlayStore(context: Context) {
    val packageName = context.packageName
    val marketIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        "https://play.google.com/store/apps/details?id=$packageName".toUri()
    )
    runCatching {
        context.startActivity(marketIntent)
    }.onFailure {
        context.startActivity(browserIntent)
    }
}

private fun shareApplication(context: Context, text: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(
        Intent.createChooser(
            shareIntent,
            context.getString(R.string.rate_app_share_chooser_title)
        )
    )
}

private fun sendFeedbackEmail(context: Context, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:${QrezzyRateAppDefaults.EMAIL_ADDRESS}".toUri()
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
}

private object QrezzyRateAppDefaults {
    val IconSize = 40.dp
    const val EMAIL_ADDRESS = "slawek.mazur.software@gmail.com"
}
