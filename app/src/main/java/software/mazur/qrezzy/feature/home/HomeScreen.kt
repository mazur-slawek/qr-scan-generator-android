package software.mazur.qrezzy.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import software.mazur.qrezzy.feature.generator.GeneratorScreen
import software.mazur.qrezzy.feature.history.HistoryNavHost
import software.mazur.qrezzy.feature.history.components.HistoryEmptyAction
import software.mazur.qrezzy.feature.scanner.ScannerScreen
import software.mazur.qrezzy.feature.settings.navigation.SettingsNavHost

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            QrezzyBottomNavigationBar(selectedTab = selectedTab, onTabSelected = viewModel::onTabSelected)
        }
    ) { innerPadding ->
        QrezzyHomeScreenContent(
            selectedTab = selectedTab,
            onTabSelected = viewModel::onTabSelected,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun QrezzyBottomNavigationBar(selectedTab: HomeTab, onTabSelected: (HomeTab) -> Unit) {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    AnimatedVisibility(visible = !isKeyboardVisible, enter = fadeIn()) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = HomeScreenDefaults.NavigationBar.tonalElevation,
            modifier = Modifier.shadow(elevation = HomeScreenDefaults.NavigationBar.shadowElevation)
        ) {
            HomeTab.entries.forEach { tab ->
                val title = stringResource(id = tab.titleResId)
                val isSelected = selectedTab == tab
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = tab.selectedIconColor,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = tab.indicatorColor,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIconColor = MaterialTheme.colorScheme.surfaceTint,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceTint
                    ),
                    icon = {
                        Icon(imageVector = tab.icon, contentDescription = title)
                    },
                    label = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun QrezzyHomeScreenContent(modifier: Modifier, selectedTab: HomeTab, onTabSelected: (HomeTab) -> Unit) {
    Box(modifier = modifier) {
        when (selectedTab) {
            HomeTab.SCAN -> ScannerScreen()
            HomeTab.GENERATE -> GeneratorScreen()
            HomeTab.HISTORY -> HistoryNavHost(
                onEmptyActionClick = { action ->
                    when (action) {
                        HistoryEmptyAction.Scan -> onTabSelected(HomeTab.SCAN)
                        HistoryEmptyAction.Generate -> onTabSelected(HomeTab.GENERATE)
                    }
                }
            )

            HomeTab.SETTINGS -> SettingsNavHost()
        }
    }
}

private object HomeScreenDefaults {
    object NavigationBar {
        val shadowElevation = 12.dp
        val tonalElevation = 0.dp
    }
}
