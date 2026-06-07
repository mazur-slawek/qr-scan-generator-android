package software.mazur.qrezzy.feature.scanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.mazur.qrezzy.R

@Composable
fun ScannerPopup(isPermissionDenied: Boolean) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .border(width = 1.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 13.dp),
    ) {
        Image(
            painter = painterResource(
                id = if (isPermissionDenied) R.drawable.scanner_popup_permission_denied else R.drawable.scanner_popup_idle),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(
                    if (isPermissionDenied) R.string.scanner_popup_permission_denied_title else R.string.scanner_popup_idle_title)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.alpha(alpha = 0.5f),
                fontSize = 14.sp,
                lineHeight = 16.sp,
                text = stringResource(
                    if (isPermissionDenied) R.string.scanner_popup_permission_denied_desc else R.string.scanner_popup_idle_desc)
            )
        }
    }
}