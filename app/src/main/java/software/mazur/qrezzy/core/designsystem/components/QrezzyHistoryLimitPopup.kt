package software.mazur.qrezzy.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark

@Composable
fun QrezzyHistoryLimitPopup(modifier: Modifier = Modifier) {
    QrezzyAnimatedStars(
        starsCount = QrezzyHistoryLimitPopupDefaults.STARS_COUNT,
        modifier = modifier
            .fillMaxWidth()
            .height(QrezzyHistoryLimitPopupDefaults.height)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = QrezzyHistoryLimitPopupDefaults.Border.width,
                    color = QrezzyYellowDark.copy(alpha = QrezzyHistoryLimitPopupDefaults.Border.ALPHA),
                    shape = ShapeDefaults.Medium,
                )
                .background(
                    color = QrezzyYellow.copy(alpha = QrezzyHistoryLimitPopupDefaults.BACKGROUND_ALPHA),
                    shape = ShapeDefaults.Medium,
                )
                .padding(QrezzyHistoryLimitPopupDefaults.contentPadding),
            horizontalArrangement = Arrangement.spacedBy(QrezzyHistoryLimitPopupDefaults.contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.history_limit_reached_title),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.height(QrezzyHistoryLimitPopupDefaults.textSpacing))
                Text(
                    text = stringResource(R.string.history_limit_reached_description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
            Image(
                painter = painterResource(R.drawable.qrezzy_mascot_max_items),
                modifier = Modifier.fillMaxHeight(),
                contentDescription = null,
            )
        }
    }
}

private object QrezzyHistoryLimitPopupDefaults {
    val height = 100.dp

    object Border {
        val width = 0.5.dp
        const val ALPHA = 0.5f
    }

    const val STARS_COUNT = 40
    const val BACKGROUND_ALPHA = 0.5f
    val contentPadding = 16.dp
    val contentSpacing = 16.dp
    val textSpacing = 6.dp
}