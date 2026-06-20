package software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyPopup
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary
import software.mazur.qrezzy.domain.qr.model.Qr

@Composable
fun QrezzyQrInfo(qr: Qr, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(QrezzyQrDetailsDefaults.sectionSpacing),
    ) {
        item {
            QrezzyQrDetailsSectionHeader(
                text = stringResource(R.string.qr_details_section_content).uppercase(),
                modifier = Modifier.padding(bottom = QrezzyQrDetailsDefaults.sectionSpacing / 2)
            )
            QrezzyQrInfoContent(qr = qr)
        }
        item {
            QrezzyQrDetailsSectionHeader(
                text = stringResource(R.string.qr_details_section_details).uppercase(),
                modifier = Modifier.padding(bottom = QrezzyQrDetailsDefaults.sectionSpacing / 2)
            )
            QrezzyQrInfoDetails(qr = qr)
        }
        item {
            QrezzyPopup(
                imageResId = R.drawable.qrezzy_security_tip,
                titleResId = R.string.qr_security_tip_title,
                descriptionResId = R.string.qr_security_tip_description
            )
        }
    }
}

@Composable
fun QrezzyQrDetailsSectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = TextSecondary,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.labelMedium,
    )
}

private object QrezzyQrDetailsDefaults {
    val sectionSpacing = 16.dp
}