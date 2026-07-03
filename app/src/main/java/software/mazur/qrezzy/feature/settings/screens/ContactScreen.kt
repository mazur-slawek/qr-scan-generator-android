package software.mazur.qrezzy.feature.settings.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
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
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun ContactScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val emailSubject = stringResource(R.string.contact_email_subject)

    Column(modifier = Modifier.padding(horizontal = ContactScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            onBackClick = onBackClick,
            titleResId = R.string.contact_screen_title,
            subtitleResId = R.string.contact_screen_subtitle,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = ContactScreenDefaults.STAR_COUNT,
                    modifier = Modifier.height(ContactScreenDefaults.imageHeight),
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_contact),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(ContactScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }

            item {
                QrezzyFieldWrapper {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.MailOutline,
                            iconSize = ContactScreenDefaults.iconSize,
                            title = stringResource(R.string.contact_email_title),
                            subtitle = stringResource(R.string.contact_email_subtitle),
                            iconTintColor = QrezzyYellowDark,
                            iconBackgroundColor = QrezzyYellowDark,
                            showDivider = false,
                            onClick = { sendEmail(context = context, subject = emailSubject) }
                        )
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
    const val STAR_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp
}