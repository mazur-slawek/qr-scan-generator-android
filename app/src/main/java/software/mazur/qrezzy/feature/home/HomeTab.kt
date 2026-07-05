package software.mazur.qrezzy.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow

enum class HomeTab(
    val titleResId: Int,
    val icon: ImageVector,
    val indicatorColor: Color,
    val selectedIconColor: Color,
) {
    SCAN(
        titleResId = R.string.navigation_tab_scan,
        icon = Icons.Rounded.QrCodeScanner,
        indicatorColor = QrezzyMint,
        selectedIconColor = QrezzyPinkDark,
    ),
    GENERATE(
        titleResId = R.string.navigation_tab_generate,
        icon = Icons.Rounded.QrCode,
        indicatorColor = QrezzyYellow,
        selectedIconColor = QrezzyPurpleDark,
    ),
    HISTORY(
        titleResId = R.string.navigation_tab_history,
        icon = Icons.Rounded.History,
        indicatorColor = QrezzyPink,
        selectedIconColor = QrezzyMintDark,
    ),
    SETTINGS(
        titleResId = R.string.navigation_tab_settings,
        icon = Icons.Rounded.Settings,
        indicatorColor = QrezzyPurple,
        selectedIconColor = QrezzyYellow,
    ),
}