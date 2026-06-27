package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun PermissionsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(PermissionsScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_permissions_screen_title,
            subtitleResId = R.string.settings_permissions_screen_subtitle,
            onBackClick = onBackClick
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = PermissionsScreenDefaults.START_COUNT,
                    modifier = Modifier.height(PermissionsScreenDefaults.imageHeight)
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_permissions),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(PermissionsScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }
            item {
                QrezzyFieldWrapper {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.CameraAlt,
                            iconSize = PermissionsScreenDefaults.iconSize,
                            title = stringResource(R.string.permissions_camera_title),
                            subtitle = stringResource(R.string.permissions_camera_subtitle),
                            iconTintColor = QrezzyPurpleDark,
                            iconBackgroundColor = QrezzyPurpleDark
                        )
                    }
                }
            }
        }
    }
}

private object PermissionsScreenDefaults {
    const val START_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp
}