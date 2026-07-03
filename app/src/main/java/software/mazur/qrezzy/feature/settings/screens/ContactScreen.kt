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
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun ContactScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val emailSubject = stringResource(R.string.contact_email_subject)
    Column(modifier = Modifier.padding(horizontal = ContactScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.contact_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = ContactScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ContactScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_contact),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ContactScreenDefaults.imageTopPadding)
                            .height(ContactScreenDefaults.imageHeight),
                        contentDescription = null,
                    )
                }
                item {
                    QrezzyFieldWrapper(title = stringResource(R.string.contact_screen_subtitle)) {
                        Column {
                            SettingsItem(
                                iconPainter = painterResource(R.drawable.qrezzy_email),
                                iconSize = ContactScreenDefaults.iconSize,
                                title = stringResource(R.string.contact_email_title),
                                subtitle = stringResource(R.string.contact_email_subtitle),
                                iconBackgroundColor = QrezzyPurpleDark,
                                showDivider = false,
                                onClick = { sendEmail(context = context, subject = emailSubject) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun sendEmail(context: Context, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:${ContactScreenDefaults.EMAIL_ADDRESS}".toUri()
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
}

private object ContactScreenDefaults {
    const val EMAIL_ADDRESS = "slawek.mazur.software@gmail.com"
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}