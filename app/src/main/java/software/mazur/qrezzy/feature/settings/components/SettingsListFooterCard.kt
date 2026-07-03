package software.mazur.qrezzy.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary

@Composable
fun SettingsListFooterCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    QrezzyAnimatedStars(
        starsCount = SettingsListFooterDefaults.STARS_COUNT,
        modifier = modifier
            .fillMaxWidth()
            .height(SettingsListFooterDefaults.height)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = SettingsListFooterDefaults.Border.width,
                    color = QrezzyMintDark.copy(alpha = SettingsListFooterDefaults.Border.ALPHA),
                    shape = ShapeDefaults.Medium,
                )
                .background(
                    color = QrezzyMint.copy(alpha = SettingsListFooterDefaults.BACKGROUND_ALPHA),
                    shape = ShapeDefaults.Medium,
                )
                .padding(SettingsListFooterDefaults.contentPadding),
            horizontalArrangement = Arrangement.spacedBy(SettingsListFooterDefaults.contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FooterText(
                        text = stringResource(R.string.settings_footer_thanks),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.size(SettingsListFooterDefaults.smallSpacing))
                    Icon(
                        painter = painterResource(R.drawable.qrezzy_heart_purple),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(SettingsListFooterDefaults.titleHeartSize),
                    )
                }
                Spacer(Modifier.height(SettingsListFooterDefaults.textSpacing))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FooterText(
                        text = stringResource(R.string.settings_footer_made_with),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.size(SettingsListFooterDefaults.tinySpacing))
                    Icon(
                        painter = painterResource(R.drawable.qrezzy_heart_red),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(SettingsListFooterDefaults.bodyHeartSize),
                    )
                    Spacer(Modifier.size(SettingsListFooterDefaults.tinySpacing))
                    FooterText(
                        text = stringResource(R.string.settings_footer_author),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.qrezzy_mascot_donate),
                modifier = Modifier.fillMaxHeight(),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun FooterText(text: String, style: androidx.compose.ui.text.TextStyle) {
    Text(text = text, color = TextPrimary, fontWeight = FontWeight.SemiBold, style = style)
}

private object SettingsListFooterDefaults {
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
    val smallSpacing = 5.dp
    val tinySpacing = 3.dp
    val titleHeartSize = 20.dp
    val bodyHeartSize = 16.dp
}