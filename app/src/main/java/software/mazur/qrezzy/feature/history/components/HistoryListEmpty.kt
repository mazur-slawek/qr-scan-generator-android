package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTabItem


enum class HistoryEmptyAction {
    Scan,
    Generate,
}

@Composable
fun HistoryListEmpty(selectedTab: QrezzyTabItem, onEmptyActionClick: (HistoryEmptyAction) -> Unit) {
    val emptyAction = selectedTab.toEmptyAction()
    val buttonTextResId = emptyAction.toButtonTextResId()

    Column(
        modifier = Modifier.padding(horizontal = HistoryListEmptyDefaults.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.qrezzy_mascot_list_empty),
            contentDescription = null,
            modifier = Modifier.height(HistoryListEmptyDefaults.imageHeight),
            contentScale = ContentScale.FillHeight
        )

        Spacer(modifier = Modifier.height(HistoryListEmptyDefaults.imageTitleSpacing))

        Text(
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            text = stringResource(R.string.history_empty_title),
        )

        Spacer(modifier = Modifier.height(HistoryListEmptyDefaults.titleSubtitleSpacing))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.history_empty_subtitle),
        )

        Spacer(modifier = Modifier.height(HistoryListEmptyDefaults.buttonPadding))

        QrezzyButton(
            onClick = { onEmptyActionClick(emptyAction) },
            text = stringResource(buttonTextResId),
            elevation = 0.dp
        )
    }
}

private fun QrezzyTabItem.toEmptyAction(): HistoryEmptyAction {
    return if (this.key == 2) HistoryEmptyAction.Generate else HistoryEmptyAction.Scan
}

private fun HistoryEmptyAction.toButtonTextResId(): Int {
    return when (this) {
        HistoryEmptyAction.Scan     -> R.string.navigation_title_scan
        HistoryEmptyAction.Generate -> R.string.navigation_title_generate
    }
}

private object HistoryListEmptyDefaults {
    val imageHeight = 170.dp
    val horizontalPadding = 32.dp
    val imageTitleSpacing = 16.dp
    val titleSubtitleSpacing = 4.dp
    val buttonPadding = 16.dp
}