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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun HistoryListEmpty() {
    Column(
        modifier = Modifier.padding(horizontal = HistoryListEmptyDefaults.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.history_empty),
            contentDescription = null,
            modifier = Modifier.height(HistoryListEmptyDefaults.imageHeight),
        )

        Spacer(modifier = Modifier.height(HistoryListEmptyDefaults.imageTitleSpacing))

        Text(
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            text = stringResource(R.string.history_empty_title),
        )

        Spacer(modifier = Modifier.height(HistoryListEmptyDefaults.titleSubtitleSpacing))

        Text(
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.history_empty_subtitle),
        )
    }
}

private object HistoryListEmptyDefaults {
    val imageHeight = 170.dp
    val horizontalPadding = 32.dp
    val imageTitleSpacing = 20.dp
    val titleSubtitleSpacing = 4.dp
}
