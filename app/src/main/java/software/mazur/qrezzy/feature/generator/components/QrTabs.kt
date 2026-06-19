package software.mazur.qrezzy.feature.generator.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.extensions.ui
import software.mazur.qrezzy.core.designsystem.theme.BorderSecondary
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.feature.generator.mapper.toQrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs

@Composable
fun QrTypeTabs(
    qrInputs: List<QrInput>,
    selectedQrInput: QrInput,
    onQrInputSelected: (QrInput) -> Unit,
) {
    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    color = BorderSecondary,
                    shape = ShapeDefaults.Medium,
                    width = QrTypeTabsDefaults.Container.borderWidth,
                ).background(
                    color = Surface,
                    shape = ShapeDefaults.Medium,
                ).padding(QrTypeTabsDefaults.Container.innerPadding),
    ) {
        val inputSize = maxWidth / qrInputs.size

        Row(
            modifier =
                Modifier
                    .width(maxWidth)
                    .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            qrInputs.forEach { input ->
                QrTypeTabButton(
                    type = input.toQrType(),
                    isSelected = selectedQrInput.isSameTypeAs(input),
                    size = inputSize,
                    onClick = { onQrInputSelected(input) },
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
    Button(
        onClick = onClick,
        modifier =
            modifier
                .size(size)
                .padding(QrTypeTabsDefaults.Tab.outerPadding)
                .border(
                    width = if (isSelected) QrTypeTabsDefaults.Tab.selectedBorderWidth else 0.dp,
                    color = if (isSelected) type.ui.containerColor else Color.Transparent,
                    shape = ShapeDefaults.Medium.copy(CornerSize(10.dp)),
                ),
        contentPadding = PaddingValues.Zero,
        shape = ShapeDefaults.Medium.copy(CornerSize(10.dp)),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = if (isSelected) type.ui.contentColor else Color.Transparent,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = type.ui.icon,
                contentDescription = null,
                tint = if (isSelected) TextPrimary else TextSecondary,
            )
            Spacer(modifier = Modifier.height(QrTypeTabsDefaults.Tab.iconTextSpacing))
            Text(
                text = stringResource(type.ui.labelResId),
                maxLines = QrTypeTabsDefaults.Tab.LABEL_MAX_LINES,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            )
        }
    }
}

private object QrTypeTabsDefaults {
    object Container {
        val innerPadding = 1.5.dp
        val borderWidth = 1.5.dp
    }

    object Tab {
        val outerPadding = 1.dp
        val iconTextSpacing = 3.dp
        val selectedBorderWidth = 1.dp
        const val LABEL_MAX_LINES = 1
    }
}
