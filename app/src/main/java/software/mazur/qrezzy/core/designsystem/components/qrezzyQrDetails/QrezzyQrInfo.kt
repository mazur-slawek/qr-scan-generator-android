package software.mazur.qrezzy.core.designsystem.components.qrezzyQrDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyPopup
import software.mazur.qrezzy.domain.qr.model.Qr

@Composable
fun QrezzyQrInfo(qr: Qr, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(QrezzyQrDetailsDefaults.sectionSpacing))
        QrezzyQrInfoContent(qr = qr)
        Spacer(modifier = Modifier.height(QrezzyQrDetailsDefaults.sectionSpacing))
        QrezzyQrInfoDetails(qr = qr)
        Spacer(modifier = Modifier.height(QrezzyQrDetailsDefaults.sectionSpacing))
        QrezzyPopup(
            imageResId = R.drawable.qrezzy_mascot_privacy,
            titleResId = R.string.qr_security_tip_title,
            descriptionResId = R.string.qr_security_tip_description
        )
    }
}

private object QrezzyQrDetailsDefaults {
    val sectionSpacing = 16.dp
}