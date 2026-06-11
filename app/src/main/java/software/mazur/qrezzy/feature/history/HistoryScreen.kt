package software.mazur.qrezzy.feature.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabs
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.Error
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.generator.model.icon
import software.mazur.qrezzy.feature.generator.model.iconTintColor
import software.mazur.qrezzy.feature.generator.model.iconTintColorDark
import software.mazur.qrezzy.feature.generator.model.label

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen() {
    var selectedTab by remember { mutableStateOf(HistoryScreenDefaults.tabs.first()) }

    Column(
        modifier = Modifier.padding(horizontal = HistoryScreenDefaults.Screen.horizontalPadding)
    ) {
        QrezzyTopBar(title = stringResource(R.string.navigation_title_history)) {
            QrezzyTopBarButton(
                onClick = {},
                enabled = false,
                text = "Zaznacz",
                icon = Icons.Rounded.CheckCircleOutline,
            )
        }
        Spacer(modifier = Modifier.height(HistoryScreenDefaults.Screen.topBarTabsSpacing))
        QrezzyTabs(
            tabs = HistoryScreenDefaults.tabs,
            selectedTab = selectedTab,
            onSelect = { tab -> selectedTab = tab },
            modifier = Modifier.padding(bottom = HistoryScreenDefaults.Screen.tabsBottomPadding)
        )
        LazyColumn(contentPadding = PaddingValues(bottom = HistoryScreenDefaults.List.bottomPadding)) {
            HistoryScreenDefaults.sections.forEach { section ->
                stickyHeader {
                    HistorySectionHeader(text = section.date)
                }
                items(
                    count = section.items.size,
                    key = { index -> "${section.date}_$index" },
                ) { index ->
                    HistoryListItem(
                        item = section.items[index],
                    )
                }
            }
        }
    }
}

@Composable
private fun HistorySectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = TextSecondary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        maxLines = HistoryScreenDefaults.Header.MAX_LINES,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = HistoryScreenDefaults.Header.topPadding,
                start = HistoryScreenDefaults.Header.startPadding,
                bottom = HistoryScreenDefaults.Header.bottomPadding,
            ),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryListItem(item: HistoryItemUi, modifier: Modifier = Modifier) {
    var select by remember { mutableStateOf(false) }
    Button(
        onClick = { select = !select },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(shape = ShapeDefaults.Medium),
        shape = ShapeDefaults.Medium,
        border = BorderStroke(width = 1.5.dp, color = BorderLight),
        colors = ButtonDefaults.buttonColors(containerColor = Surface, contentColor = item.qrType.iconTintColorDark),
        contentPadding = PaddingValues.Zero,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(HistoryScreenDefaults.Item.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.qrType.icon,
                contentDescription = item.qrType.label,
                tint = TextPrimary,
                modifier = modifier
                    .border(
                        width = HistoryScreenDefaults.Icon.borderWidth,
                        color = item.qrType.iconTintColorDark,
                        shape = ShapeDefaults.Small,
                    )
                    .shadow(elevation = HistoryScreenDefaults.Icon.elevation, shape = ShapeDefaults.Small)
                    .background(color = item.qrType.iconTintColor, shape = ShapeDefaults.Small)
                    .padding(HistoryScreenDefaults.Icon.padding),
            )
            Spacer(modifier = Modifier.width(HistoryScreenDefaults.Item.iconTextSpacing))
            Column(modifier = Modifier.weight(HistoryScreenDefaults.Item.TEXT_WEIGHT)) {
                Text(
                    text = item.value,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = HistoryScreenDefaults.Item.TITLE_MAX_LINES,
                )
                Spacer(modifier = Modifier.height(HistoryScreenDefaults.Item.textSpacing))
                Text(
                    text = item.subtitle,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = HistoryScreenDefaults.Item.SUBTITLE_MAX_LINES,
                )
            }
            Icon(
                imageVector = if (select) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                tint = if (select) Error else TextSecondary,
                contentDescription = null,
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = TextSecondary,
                contentDescription = null,
            )
        }
    }
}

@Immutable
private data class HistorySectionUi(
    val date: String,
    val items: List<HistoryItemUi>,
)

@Immutable
private data class HistoryItemUi(
    val qrType: QrType,
    val value: String,
    val subtitle: String,
)

private object HistoryScreenDefaults {
    val tabs = listOf(
        QrezzyTabItem(key = 0, title = "All"),
        QrezzyTabItem(key = 1, title = "Scanned"),
        QrezzyTabItem(key = 2, title = "Generated"),
    )
    val sections = listOf(
        HistorySectionUi(
            date = "08.06.2026",
            items = listOf(
                HistoryItemUi(QrType.Text(), "http://test.pl", "Scanned - 12:23"),
                HistoryItemUi(QrType.Url(), "https://qrezzy.app", "Generated - 13:40"),
                HistoryItemUi(QrType.Wifi(), "Home Wi-Fi", "Generated - 15:12"),
                HistoryItemUi(QrType.Text(), "Simple text note", "Scanned - 18:21"),
            ),
        ),
        HistorySectionUi(
            date = "09.06.2026",
            items = listOf(
                HistoryItemUi(QrType.Contact(), "Sławek Mazur", "Generated - 09:12"),
                HistoryItemUi(QrType.Url(), "https://github.com", "Scanned - 10:44"),
                HistoryItemUi(QrType.Text(), "Portfolio QR", "Generated - 12:01"),
                HistoryItemUi(QrType.Phone(), "+48 000 000 000", "Scanned - 16:32"),
            ),
        ),
        HistorySectionUi(
            date = "10.06.2026",
            items = listOf(
                HistoryItemUi(QrType.Email(), "hello@qrezzy.app", "Generated - 08:11"),
                HistoryItemUi(QrType.Wifi(), "Office Wi-Fi", "Scanned - 11:27"),
                HistoryItemUi(QrType.Contact(), "QREZZY Support", "Generated - 14:18"),
                HistoryItemUi(QrType.Text(), "QR note", "Scanned - 19:02"),
            ),
        ),
    )

    object Screen {
        val horizontalPadding = 16.dp
        val topBarTabsSpacing = 16.dp
        val tabsBottomPadding = 2.dp
    }

    object List {
        val bottomPadding = 16.dp
    }

    object Header {
        val topPadding = 10.dp
        val startPadding = 4.dp
        val bottomPadding = 2.dp
        const val MAX_LINES = 1
    }

    object Item {
        val contentPadding = 16.dp
        val iconTextSpacing = 16.dp
        val textSpacing = 2.dp
        const val TEXT_WEIGHT = 1f
        const val TITLE_MAX_LINES = 1
        const val SUBTITLE_MAX_LINES = 1
    }

    object Icon {
        val borderWidth = 1.5.dp
        val elevation = 1.5.dp
        val padding = 8.dp
    }
}