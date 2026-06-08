package software.mazur.qrezzy.feature.generator.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.generator.model.icon
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import software.mazur.qrezzy.feature.generator.model.label

@Composable
fun QrTypeTabs(
    selectedType: QrType,
    onTypeSelected: (QrType) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .border(
                color = BorderPrimary,
                shape = ShapeDefaults.Medium,
                width = QrTypeTabsDefaults.Container.borderWidth,
            )
            .background(
                color = Surface,
                shape = ShapeDefaults.Medium,
            )
            .padding(QrTypeTabsDefaults.Container.innerPadding),
    ) {
        val tabs = QrTypeTabsDefaults.tabs
        val tabSize = maxWidth / tabs.size

        Row(
            modifier = Modifier
                .width(maxWidth)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            tabs.forEach { type ->
                QrTypeTabButton(
                    type = type,
                    isSelected = selectedType.isSameTypeAs(type),
                    size = tabSize,
                    onClick = { onTypeSelected(type) },
                )
            }
        }
    }
}

@Composable
private fun QrTypeTabButton(
    type: QrType,
    isSelected: Boolean,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QrTypeTabColors.resolve(isSelected = isSelected)
    Button(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .padding(QrTypeTabsDefaults.Tab.outerPadding)
            .border(
                width = if (isSelected) {
                    QrTypeTabsDefaults.Tab.selectedBorderWidth
                } else {
                    QrTypeTabsDefaults.Tab.unselectedBorderWidth
                },
                color = colors.borderColor,
                shape = ShapeDefaults.Medium.copy(CornerSize(10.dp)),
            ),
        contentPadding = PaddingValues.Zero,
        shape = ShapeDefaults.Medium.copy(CornerSize(10.dp)),
        border = BorderStroke(
            width = QrTypeTabsDefaults.Tab.buttonBorderWidth,
            color = Color.Transparent,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(imageVector = type.icon, contentDescription = null, tint = colors.iconColor)
            Spacer(modifier = Modifier.height(QrTypeTabsDefaults.Tab.iconTextSpacing))
            Text(
                text = type.label,
                maxLines = QrTypeTabsDefaults.Tab.LABEL_MAX_LINES,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Immutable
private data class QrTypeTabColors(
    val containerColor: Color,
    val contentColor: Color,
    val iconColor: Color,
    val borderColor: Color,
) {
    companion object {
        fun resolve(isSelected: Boolean): QrTypeTabColors {
            return if (isSelected) {
                QrTypeTabColors(
                    containerColor = QrezzyMint,
                    contentColor = TextPrimary,
                    iconColor = QrezzyPurpleDark,
                    borderColor = QrezzyMintDark,
                )
            } else {
                QrTypeTabColors(
                    containerColor = Surface,
                    contentColor = TextSecondary,
                    iconColor = TextSecondary,
                    borderColor = Color.Transparent,
                )
            }
        }
    }
}

private object QrTypeTabsDefaults {
    val tabs = listOf(
        QrType.Text(),
        QrType.Url(),
        QrType.Wifi(),
        QrType.Contact(),
        QrType.Email(),
        QrType.Phone(),
    )

    object Container {
        val innerPadding = 1.5.dp
        val borderWidth = 1.5.dp
    }

    object Tab {
        val outerPadding = 1.dp
        val iconTextSpacing = 3.dp
        val selectedBorderWidth = 1.dp
        val unselectedBorderWidth = 0.dp
        val buttonBorderWidth = 0.dp
        const val LABEL_MAX_LINES = 1
    }
}