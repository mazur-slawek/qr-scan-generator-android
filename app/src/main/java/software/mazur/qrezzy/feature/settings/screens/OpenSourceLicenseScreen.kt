package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyFieldWrapper
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.feature.settings.components.SettingsItem

@Composable
fun OpenSourceLicenseScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = OpenSourceLicenseScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(
            titleResId = R.string.settings_open_source_screen_title,
            subtitleResId = R.string.settings_open_source_screen_subtitle,
            onBackClick = onBackClick,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                QrezzyAnimatedStars(
                    starsCount = OpenSourceLicenseScreenDefaults.STAR_COUNT,
                    modifier = Modifier.height(OpenSourceLicenseScreenDefaults.imageHeight),
                ) {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_open_source_license),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(OpenSourceLicenseScreenDefaults.imagePadding)
                            .fillMaxHeight(),
                    )
                }
            }

            item {
                QrezzyFieldWrapper {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.QrCodeScanner,
                            iconSize = OpenSourceLicenseScreenDefaults.iconSize,
                            title = stringResource(R.string.license_zxing_title),
                            subtitle = stringResource(R.string.license_apache_2),
                            trailing = {
                                LicenseVersionBadge(version = stringResource(R.string.license_zxing_version))
                            },
                            iconTintColor = QrezzyMintDark,
                            iconBackgroundColor = QrezzyMintDark,
                        )
                        SettingsItem(
                            icon = Icons.Outlined.CenterFocusStrong,
                            iconSize = OpenSourceLicenseScreenDefaults.iconSize,
                            title = stringResource(R.string.license_mlkit_barcode_title),
                            subtitle = stringResource(R.string.license_apache_2),
                            trailing = {
                                LicenseVersionBadge(
                                    version = stringResource(R.string.license_mlkit_barcode_version))
                            },
                            iconTintColor = QrezzyPinkDark,
                            iconBackgroundColor = QrezzyPinkDark,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LicenseVersionBadge(version: String) {
    Box(
        modifier = Modifier
            .background(
                color = QrezzyPurpleDark.copy(alpha = OpenSourceLicenseScreenDefaults.Badge.backgroundAlpha),
                shape = ShapeDefaults.Small,
            )
            .padding(
                horizontal = OpenSourceLicenseScreenDefaults.Badge.horizontalPadding,
                vertical = OpenSourceLicenseScreenDefaults.Badge.verticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = version,
            color = QrezzyPurpleDark,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private object OpenSourceLicenseScreenDefaults {
    const val STAR_COUNT = 50
    val imageHeight = 180.dp
    val imagePadding = 16.dp
    val horizontalPadding = 16.dp
    val iconSize = 40.dp

    object Badge {
        val horizontalPadding = 6.dp
        val verticalPadding = 2.dp
        const val backgroundAlpha = 0.12f
    }
}