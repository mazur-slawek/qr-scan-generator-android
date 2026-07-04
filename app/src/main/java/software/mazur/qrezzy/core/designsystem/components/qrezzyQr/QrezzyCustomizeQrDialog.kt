package software.mazur.qrezzy.core.designsystem.components.qrezzyQr

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBarButton
import software.mazur.qrezzy.core.designsystem.extensions.toComposeColor
import software.mazur.qrezzy.core.designsystem.theme.BorderPrimary
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrezzyCustomizeQrDialog(
    qrBitmap: Bitmap?,
    style: QrStyle,
    isEditMode: Boolean = false,
    onQrColorSelected: (Long) -> Unit,
    onBackgroundColorSelected: (Long) -> Unit,
    onPatternStyleSelected: (QrPatternStyle) -> Unit,
    onErrorCorrectionSelected: (QrErrorCorrection) -> Unit,
    onCancelClick: () -> Unit,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
) {
    AlertDialog(onDismissRequest = onCancelClick, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = CustomizeQrDialogDefaults.Container.outerVerticalPadding,
                    horizontal = CustomizeQrDialogDefaults.Container.outerHorizontalPadding,
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CustomizeQrDialogDefaults.Container.shape,
                )
                .border(
                    color = BorderPrimary,
                    shape = CustomizeQrDialogDefaults.Container.shape,
                    width = CustomizeQrDialogDefaults.Container.borderWidth,
                )
                .clip(CustomizeQrDialogDefaults.Container.shape)
                .padding(CustomizeQrDialogDefaults.Container.contentPadding),
        ) {
            QrezzyTopBar(titleResId = R.string.customize_qr_title, subtitleResId = R.string.customize_qr_subtitle) {
                QrezzyTopBarButton(onClick = onCancelClick, iconPainter = painterResource(R.drawable.qrezzy_close))
            }
            QrezzyQrPreview(qrBitmap = qrBitmap)
            LazyColumn(modifier = Modifier.weight(CustomizeQrDialogDefaults.CONTENT_WEIGHT)) {
                item { Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Section.firstTopSpacing)) }
                item {
                    CustomizeQrColorSelector(
                        title = stringResource(R.string.customize_qr_qr_color),
                        colors = CustomizeQrDialogDefaults.Colors.qrColors,
                        selectedColor = style.qrColor,
                        onColorSelected = onQrColorSelected,
                    )
                }
                item { Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Section.spacing)) }
                item {
                    CustomizeQrColorSelector(
                        title = stringResource(R.string.customize_qr_background_color),
                        colors = CustomizeQrDialogDefaults.Colors.backgroundColors,
                        selectedColor = style.backgroundColor,
                        onColorSelected = onBackgroundColorSelected,
                    )
                }
                item { Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Section.spacing)) }
                item {
                    CustomizeQrPatternSelector(
                        selectedPatternStyle = style.patternStyle,
                        onPatternStyleSelected = onPatternStyleSelected,
                    )
                }
                item { Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Section.spacing)) }
                item {
                    CustomizeQrErrorCorrectionSelector(
                        selectedErrorCorrection = style.errorCorrection,
                        onErrorCorrectionSelected = onErrorCorrectionSelected,
                    )
                }
                item { Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Section.bottomSpacing)) }
            }
            CustomizeQrActions(
                isEditMode = isEditMode,
                onResetClick = onResetClick,
                onApplyClick = onApplyClick
            )
        }
    }
}

@Composable
private fun CustomizeQrColorSelector(
    title: String,
    selectedColor: Long,
    colors: List<QrColorOption>,
    onColorSelected: (Long) -> Unit
) {
    QrezzyListSection(title = title) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
                .padding(all = CustomizeQrDialogDefaults.Selector.spacing),
            horizontalArrangement = Arrangement.spacedBy(CustomizeQrDialogDefaults.Selector.spacing)
        ) {
            colors.forEach { color ->
                CustomizeQrColorOption(
                    color = color,
                    isSelected = selectedColor == color.value,
                    onClick = { onColorSelected(color.value) },
                )
            }
        }
    }
}

@Composable
private fun CustomizeQrColorOption(color: QrColorOption, isSelected: Boolean, onClick: () -> Unit) {
    Spacer(
        modifier = Modifier
            .size(CustomizeQrDialogDefaults.ColorOption.containerSize)
            .border(
                width = if (isSelected) {
                    CustomizeQrDialogDefaults.ColorOption.selectedOuterBorderWidth
                } else {
                    CustomizeQrDialogDefaults.ColorOption.outerBorderWidth
                },
                shape = CustomizeQrDialogDefaults.ColorOption.containerShape,
                color = if (isSelected) QrezzyPurpleDark else BorderPrimary
            )
            .background(
                color = if (isSelected) QrezzyPurple else Color.Transparent,
                shape = CustomizeQrDialogDefaults.ColorOption.containerShape
            )
            .padding(
                all = if (isSelected) {
                    CustomizeQrDialogDefaults.ColorOption.selectedInnerPadding
                } else {
                    CustomizeQrDialogDefaults.ColorOption.defaultInnerPadding
                }
            )
            .background(
                color = color.value.toComposeColor(),
                shape = if (isSelected) {
                    CustomizeQrDialogDefaults.ColorOption.selectedColorShape
                } else {
                    CustomizeQrDialogDefaults.ColorOption.containerShape
                }
            )
            .border(
                color = if (isSelected) BorderPrimary else Color.Transparent,
                width = if (isSelected) {
                    CustomizeQrDialogDefaults.ColorOption.selectedInnerBorderWidth
                } else {
                    CustomizeQrDialogDefaults.ColorOption.defaultInnerBorderWidth
                },
                shape = CustomizeQrDialogDefaults.ColorOption.selectedColorShape
            )
            .clip(CustomizeQrDialogDefaults.ColorOption.containerShape)
            .clickable(onClick = onClick),
    )
}

@Composable
private fun CustomizeQrPatternSelector(
    selectedPatternStyle: QrPatternStyle,
    onPatternStyleSelected: (QrPatternStyle) -> Unit,
) {
    QrezzyListSection(title = stringResource(R.string.customize_qr_pattern_style)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
                .padding(all = CustomizeQrDialogDefaults.Selector.spacing),
            horizontalArrangement = Arrangement.spacedBy(CustomizeQrDialogDefaults.Selector.spacing)
        ) {
            CustomizeQrOption(
                iconResId = R.drawable.square,
                titleResId = R.string.customize_qr_pattern_square,
                isSelected = selectedPatternStyle == QrPatternStyle.SQUARE,
                onClick = { onPatternStyleSelected(QrPatternStyle.SQUARE) }
            )
            CustomizeQrOption(
                iconResId = R.drawable.rounded,
                titleResId = R.string.customize_qr_pattern_rounded,
                isSelected = selectedPatternStyle == QrPatternStyle.ROUNDED,
                onClick = { onPatternStyleSelected(QrPatternStyle.ROUNDED) }
            )
            CustomizeQrOption(
                iconResId = R.drawable.dots,
                titleResId = R.string.customize_qr_pattern_dots,
                isSelected = selectedPatternStyle == QrPatternStyle.DOTS,
                onClick = { onPatternStyleSelected(QrPatternStyle.DOTS) }
            )
        }
    }
}

@Composable
private fun CustomizeQrErrorCorrectionSelector(
    selectedErrorCorrection: QrErrorCorrection,
    onErrorCorrectionSelected: (QrErrorCorrection) -> Unit,
) {
    QrezzyListSection(title = stringResource(R.string.customize_qr_error_correction)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
                .padding(all = CustomizeQrDialogDefaults.Selector.spacing),
            horizontalArrangement = Arrangement.spacedBy(CustomizeQrDialogDefaults.Selector.spacing)
        ) {
            CustomizeQrOption(
                iconResId = R.drawable.qr_error_correction,
                titleResId = R.string.customize_qr_error_low,
                isSelected = selectedErrorCorrection == QrErrorCorrection.LOW,
                onClick = { onErrorCorrectionSelected(QrErrorCorrection.LOW) },
            )
            CustomizeQrOption(
                iconResId = R.drawable.qr_error_correction,
                titleResId = R.string.customize_qr_error_medium,
                isSelected = selectedErrorCorrection == QrErrorCorrection.MEDIUM,
                onClick = { onErrorCorrectionSelected(QrErrorCorrection.MEDIUM) },
            )
            CustomizeQrOption(
                iconResId = R.drawable.qr_error_correction,
                titleResId = R.string.customize_qr_error_quartile,
                isSelected = selectedErrorCorrection == QrErrorCorrection.QUARTILE,
                onClick = { onErrorCorrectionSelected(QrErrorCorrection.QUARTILE) },
            )
            CustomizeQrOption(
                iconResId = R.drawable.qr_error_correction,
                titleResId = R.string.customize_qr_error_high,
                isSelected = selectedErrorCorrection == QrErrorCorrection.HIGH,
                onClick = { onErrorCorrectionSelected(QrErrorCorrection.HIGH) },
            )
        }
    }
}

@Composable
private fun CustomizeQrOption(iconResId: Int, titleResId: Int, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(CustomizeQrDialogDefaults.Option.width)
            .height(CustomizeQrDialogDefaults.Option.height)
            .border(
                width = CustomizeQrDialogDefaults.Option.borderWidth,
                shape = CustomizeQrDialogDefaults.Option.shape,
                color = if (isSelected) QrezzyPurpleDark else TextSecondary,
            )
            .background(
                color = if (isSelected) QrezzyPurple else Color.Transparent,
                shape = CustomizeQrDialogDefaults.Option.shape,
            )
            .clip(CustomizeQrDialogDefaults.Option.shape)
            .clickable(onClick = onClick)
            .padding(
                vertical = CustomizeQrDialogDefaults.Option.verticalPadding,
                horizontal = CustomizeQrDialogDefaults.Option.horizontalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = if (isSelected) TextPrimary else TextSecondary,
            modifier = Modifier.size(CustomizeQrDialogDefaults.Option.iconSize),
        )
        Spacer(modifier = Modifier.width(CustomizeQrDialogDefaults.Option.iconTextSpacing))
        Text(
            text = stringResource(titleResId),
            color = if (isSelected) TextPrimary else TextSecondary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(CustomizeQrDialogDefaults.CONTENT_WEIGHT),
        )
    }
}

@Composable
private fun CustomizeQrActions(
    isEditMode: Boolean = false,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    QrezzyButton(
        text = stringResource(id = if (isEditMode) R.string.customize_qr_save else R.string.customize_qr_apply),
        elevation = CustomizeQrDialogDefaults.Actions.buttonElevation,
        onClick = onApplyClick,
    )
    Spacer(modifier = Modifier.height(CustomizeQrDialogDefaults.Actions.buttonSpacing))
    QrezzyButton(
        text = stringResource(R.string.customize_qr_reset),
        containerColor = Surface,
        depthColor = QrezzyPink,
        elevation = CustomizeQrDialogDefaults.Actions.buttonElevation,
        onClick = onResetClick,
    )
}

private data class QrColorOption(
    val value: Long
)

private object CustomizeQrDialogDefaults {
    const val CONTENT_WEIGHT = 1f

    object Container {
        val shape = ShapeDefaults.Large
        val outerHorizontalPadding = 16.dp
        val outerVerticalPadding = 5.dp
        val contentPadding = 16.dp
        val borderWidth = 2.dp
    }

    object Section {
        val firstTopSpacing = 8.dp
        val spacing = 16.dp
        val bottomSpacing = 32.dp
    }

    object Selector {
        val spacing = 10.dp
    }

    object ColorOption {
        val containerSize = 48.dp
        val containerShape = ShapeDefaults.Medium
        val selectedColorShape = RoundedCornerShape(9.dp)
        val outerBorderWidth = 1.dp
        val selectedOuterBorderWidth = 1.5.dp
        val defaultInnerPadding = 0.dp
        val selectedInnerPadding = 5.dp
        val defaultInnerBorderWidth = 0.dp
        val selectedInnerBorderWidth = 1.5.dp
    }

    object Option {
        val width = 112.dp
        val height = 48.dp
        val shape = ShapeDefaults.Medium
        val borderWidth = 2.dp
        val verticalPadding = 8.dp
        val horizontalPadding = 10.dp
        val iconSize = 32.dp
        val iconTextSpacing = 8.dp
        const val SELECTED_BACKGROUND_ALPHA = 0.2f
    }

    object Actions {
        val buttonSpacing = 8.dp
        val buttonElevation = 0.dp
    }

    object Colors {
        val qrColors = listOf(
            0xFF000000, 0xFF212121, 0xFF424242, 0xFF607D8B, 0xFF455A64, 0xFF7A3DE0, 0xFF8B5CF6, 0xFF673AB7, 0xFF512DA8,
            0xFFF54081, 0xFFEC4899, 0xFFE91E63, 0xFFC2185B, 0xFF25AD67, 0xFF10B981, 0xFF4CAF50, 0xFF2E7D32, 0xFF2196F3,
            0xFF3B82F6, 0xFF1976D2, 0xFF0D47A1, 0xFF00BCD4, 0xFF0097A7, 0xFFFF9800, 0xFFF57C00, 0xFFFFC107, 0xFFFFEB3B,
            0xFFE53935, 0xFFD32F2F, 0xFFB71C1C, 0xFF795548, 0xFF5D4037
        ).map(::QrColorOption)
        val backgroundColors = listOf(
            0xFFFFFFFF, 0xFFF8FAFC, 0xFFF5F5F5, 0xFFE0E0E0, 0xFFFFF8E1, 0xFFFFFDE7, 0xFFFFF3E0, 0xFFFFE0B2, 0xFFFFF7ED,
            0xFFFFEDD5, 0xFFFCE4EC, 0xFFF8BBD0, 0xFFFDF2F8, 0xFFFCE7F3, 0xFFEDE7F6, 0xFFD1C4E9, 0xFFF5F3FF, 0xFFEDE9FE,
            0xFFE8F5E9, 0xFFC8E6C9, 0xFFECFDF5, 0xFFD1FAE5, 0xFFE0F7FA, 0xFFB2EBF2, 0xFFE3F2FD, 0xFFBBDEFB, 0xFFEFF6FF,
            0xFFDBEAFE, 0xFFFFEBEE, 0xFFFFCDD2, 0xFFFBE9E7, 0xFFFFCCBC
        ).map(::QrColorOption)
    }
}
