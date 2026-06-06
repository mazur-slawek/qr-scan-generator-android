package software.mazur.qrezzy.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ManageHistory
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.Mint
import software.mazur.qrezzy.core.designsystem.theme.Pink
import software.mazur.qrezzy.core.designsystem.theme.Purple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.core.designsystem.theme.Yellow
import software.mazur.qrezzy.feature.generator.GeneratorScreen
import software.mazur.qrezzy.feature.history.HistoryScreen
import software.mazur.qrezzy.feature.scanner.ScannerScreen
import software.mazur.qrezzy.feature.settings.SettingsScreen

@Composable
fun HomeScreen() {
    val selectedTabIndex = remember { mutableIntStateOf(HomeScreenDefaults.INITIAL_TAB_INDEX) }

    Scaffold(
        bottomBar = {
            QrezzyBottomNavigationBar(
                selectedTab = HomeTab.entries[selectedTabIndex.intValue],
                onTabSelected = { selectedTab ->
                    selectedTabIndex.intValue = selectedTab.ordinal
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            HomeScreenContent(
                selectedTab = HomeTab.entries[selectedTabIndex.intValue],
            )
        }
    }
}

@Composable
private fun QrezzyBottomNavigationBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
) {
    NavigationBar(
        modifier =
            Modifier.shadow(
                elevation = HomeScreenDefaults.NAVIGATION_BAR_SHADOW_ELEVATION,
            ),
        containerColor = HomeScreenDefaults.NAVIGATION_BAR_CONTAINER_COLOR,
        tonalElevation = HomeScreenDefaults.NAVIGATION_BAR_TONAL_ELEVATION,
    ) {
        HomeTab.entries.forEach { tab ->
            val title = stringResource(id = tab.titleResId)
            val isSelect = selectedTab == tab

            NavigationBarItem(
                selected = isSelect,
                onClick = { onTabSelected(tab) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = tab.selectedIconColor,
                        selectedTextColor = HomeScreenDefaults.SELECTED_TEXT_COLOR,
                        indicatorColor = tab.indicatorColor,
                        unselectedIconColor = HomeScreenDefaults.UNSELECTED_ICON_COLOR,
                        unselectedTextColor = HomeScreenDefaults.UNSELECTED_TEXT_COLOR,
                        disabledIconColor = HomeScreenDefaults.DISABLED_ICON_COLOR,
                        disabledTextColor = HomeScreenDefaults.DISABLED_TEXT_COLOR,
                    ),
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = title,
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
        }
    }
}

@Composable
private fun HomeScreenContent(selectedTab: HomeTab) {
    when (selectedTab) {
        HomeTab.SCAN -> ScannerScreen()
        HomeTab.GENERATE -> GeneratorScreen()
        HomeTab.HISTORY -> HistoryScreen()
        HomeTab.SETTINGS -> SettingsScreen()
    }
}

private enum class HomeTab(
    @StringRes val titleResId: Int,
    val icon: ImageVector,
    val indicatorColor: Color,
    val selectedIconColor: Color,
) {
    SCAN(
        titleResId = R.string.navigation_tab_scan,
        icon = Icons.Rounded.QrCodeScanner,
        indicatorColor = QrezzyMint,
        selectedIconColor = Pink,
    ),
    GENERATE(
        titleResId = R.string.navigation_tab_generate,
        icon = Icons.Rounded.QrCode,
        indicatorColor = QrezzyYellow,
        selectedIconColor = Purple,
    ),
    HISTORY(
        titleResId = R.string.navigation_tab_history,
        icon = Icons.Rounded.ManageHistory,
        indicatorColor = QrezzyPink,
        selectedIconColor = Mint,
    ),
    SETTINGS(
        titleResId = R.string.navigation_tab_settings,
        icon = Icons.Rounded.Settings,
        indicatorColor = QrezzyPurple,
        selectedIconColor = Yellow,
    ),
}

private object HomeScreenDefaults {
    const val INITIAL_TAB_INDEX = 0
    val NAVIGATION_BAR_SHADOW_ELEVATION = 12.dp
    val NAVIGATION_BAR_TONAL_ELEVATION = 0.dp
    val NAVIGATION_BAR_CONTAINER_COLOR = Color.White
    val SELECTED_TEXT_COLOR = Color.Black
    val UNSELECTED_ICON_COLOR = Color.Gray
    val UNSELECTED_TEXT_COLOR = Color.Gray
    val DISABLED_ICON_COLOR = Color.LightGray
    val DISABLED_TEXT_COLOR = Color.LightGray
}
