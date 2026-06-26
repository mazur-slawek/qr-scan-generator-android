package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun HistoryListFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HistoryListFooterDefaults.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.qrezzy_mascot_history_list_footer),
            contentDescription = null,
            modifier = Modifier.height(HistoryListFooterDefaults.imageHeight),
            contentScale = ContentScale.FillHeight
        )
        Spacer(modifier = Modifier.height(HistoryListFooterDefaults.imageTitleSpacing))
        Text(
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            text = stringResource(R.string.history_footer_title),
        )
        Spacer(modifier = Modifier.height(HistoryListFooterDefaults.titleSubtitleSpacing))
        Text(
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.history_footer_subtitle),
        )
    }
}

private object HistoryListFooterDefaults {
    val imageHeight = 140.dp
    val horizontalPadding = 32.dp
    val imageTitleSpacing = 16.dp
    val titleSubtitleSpacing = 4.dp
}