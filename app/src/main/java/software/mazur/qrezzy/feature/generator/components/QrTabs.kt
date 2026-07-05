package software.mazur.qrezzy.feature.generator.components


import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.extensions.ui
import software.mazur.qrezzy.feature.generator.mapper.toQrType
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.isSameTypeAs
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun QrTypeTabs(qrInputs: List<QrInput>, selectedQrInput: QrInput, onQrInputSelected: (QrInput) -> Unit) {
    val scrollState = rememberScrollState()

    LaunchedEffect(qrInputs.size) {
        delay(600.milliseconds)
        if (scrollState.maxValue > 0) {
            scrollState.animateScrollTo(value = scrollState.maxValue, animationSpec = tween(durationMillis = 700))
            delay(250.milliseconds)
            scrollState.animateScrollTo(value = 0, animationSpec = tween(durationMillis = 700))
        }
    }

    QrezzyListSection(title = stringResource(R.string.generator_select_qr_type)) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(state = scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            qrInputs.forEach { input ->
                val typeUi = input.toQrType().ui
                val isSelected = selectedQrInput.isSameTypeAs(input)
                Column(
                    modifier = Modifier
                        .padding(all = QrTypeTabsDefaults.padding)
                        .width(QrTypeTabsDefaults.width)
                        .height(QrTypeTabsDefaults.height)
                        .border(
                            width = if (isSelected) {
                                QrTypeTabsDefaults.selectedBorderWidth
                            } else {
                                QrTypeTabsDefaults.defaultBorderWidth
                            },
                            color = if (isSelected) typeUi.containerColor else Color.Transparent,
                            shape = QrTypeTabsDefaults.shape,
                        )
                        .background(
                            color = if (isSelected) typeUi.contentColor else Color.Transparent,
                            shape = QrTypeTabsDefaults.shape
                        )
                        .clip(shape = QrTypeTabsDefaults.shape)
                        .clickable(onClick = { onQrInputSelected(input) }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = typeUi.icon,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(QrTypeTabsDefaults.iconTextSpacing))
                    Text(
                        text = stringResource(typeUi.labelResId),
                        maxLines = QrTypeTabsDefaults.LABEL_MAX_LINES,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    )
                }
            }
        }
    }
}

private object QrTypeTabsDefaults {
    val height = 60.dp
    val width = 70.dp
    val padding = 2.5.dp
    val shape = RoundedCornerShape(8.dp)
    val iconTextSpacing = 2.dp
    val selectedBorderWidth = 1.dp
    val defaultBorderWidth = 0.dp
    const val LABEL_MAX_LINES = 1
}