package software.mazur.qrezzy.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark


data class QrezzyTabItem(
    val key: Int,
    @param:StringRes val titleResId: Int,
)

@Composable
fun QrezzyTabs(
    tabs: List<QrezzyTabItem>,
    selectedTab: QrezzyTabItem,
    onSelect: (QrezzyTabItem) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.4f)
            .background(color = MaterialTheme.colorScheme.surface, shape = ShapeDefaults.Medium)
            .border(
                width = QrezzyTabsDefaults.borderWidth,
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = ShapeDefaults.Medium,
            )
            .padding(all = 1.dp)
    ) {
        tabs.forEach { tab ->
            QrezzyTabItem(
                modifier = Modifier.weight(1f),
                text = stringResource(tab.titleResId),
                isSelected = tab.key == selectedTab.key,
                onClick = { onSelect(tab) },
                enabled = enabled,
            )
        }
    }

}

@Composable
private fun QrezzyTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .padding(QrezzyTabsDefaults.Tab.outerPadding)
                .height(QrezzyTabsDefaults.Tab.height),
        shape = ShapeDefaults.Medium.copy(all = CornerSize(QrezzyTabsDefaults.Tab.cornerRadius)),
        border =
            BorderStroke(
                width = QrezzyTabsDefaults.Tab.borderWidth,
                color = if (isSelected) QrezzyMintDark else Color.Transparent,
            ),
        contentPadding = PaddingValues.Zero,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = if (isSelected) QrezzyMint else Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}

private object QrezzyTabsDefaults {
    val borderWidth = 1.5.dp

    object Tab {
        val height = 35.dp
        val cornerRadius = 10.dp
        val borderWidth = 1.5.dp
        val outerPadding = 1.dp
    }
}
