package software.mazur.qrezzy.feature.settings.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyListItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark

@Composable
fun PermissionsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(PermissionsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.permissions_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = PermissionsScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(PermissionsScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_permissions),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = PermissionsScreenDefaults.imageTopPadding)
                            .height(PermissionsScreenDefaults.imageHeight),
                        contentDescription = null
                    )
                }
                item {
                    QrezzyListSection(title = stringResource(R.string.permissions_screen_subtitle)) {
                        Column {
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.qrezzy_camera),
                                iconSize = PermissionsScreenDefaults.iconSize,
                                title = stringResource(R.string.permissions_camera_title),
                                subtitle = stringResource(R.string.permissions_camera_subtitle),
                                iconBackgroundColor = QrezzyMintDark,
                                onClick = { openAppPermissionsSettings(context = context) }
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(PermissionsScreenDefaults.itemSpacing)) }
            }
        }
    }
}

fun openAppPermissionsSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

private object PermissionsScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}
