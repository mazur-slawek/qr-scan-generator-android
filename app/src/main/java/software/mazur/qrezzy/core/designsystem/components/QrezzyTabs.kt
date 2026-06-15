package software.mazur.qrezzy.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

data class QrezzyTabItem(
    val key: Int,
    @param:StringRes val titleResId: Int,
)

@Composable
fun QrezzyTabs(
    tabs: List<QrezzyTabItem>,
    selectedTab: QrezzyTabItem,
    onSelect: (QrezzyTabItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = QrezzyTabsDefaults.Container.elevation),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Surface, shape = ShapeDefaults.Medium)
        ) {
            tabs.forEach { tab ->
                QrezzyTabItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(tab.titleResId),
                    isSelected = tab.key == selectedTab.key,
                    onClick = { onSelect(tab) },
                )
            }
        }
    }
}

@Composable
private fun QrezzyTabItem(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(QrezzyTabsDefaults.Tab.outerPadding)
            .height(QrezzyTabsDefaults.Tab.height),
        shape = ShapeDefaults.Medium.copy(all = CornerSize(QrezzyTabsDefaults.Tab.cornerRadius)),
        border = BorderStroke(
            width = QrezzyTabsDefaults.Tab.borderWidth,
            color = if (isSelected) QrezzyMintDark else Color.Transparent
        ),
        contentPadding = PaddingValues.Zero,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) QrezzyMint else Color.Transparent,
            contentColor = TextSecondary,
        ),
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

private object QrezzyTabsDefaults {
    object Container {
        val elevation = 2.dp
    }

    object Tab {
        val height = 35.dp
        val cornerRadius = 10.dp
        val borderWidth = 1.5.dp
        val outerPadding = 1.dp
    }
}