package software.mazur.qrezzy.feature.scanner.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun ScannerPopup(isPermissionDenied: Boolean, modifier: Modifier = Modifier) {
    val content = ScannerPopupContent.resolve(isPermissionDenied = isPermissionDenied)

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .border(
                color = BorderLight,
                shape = ShapeDefaults.Medium,
                width = ScannerPopupDefaults.Border.width,
            )
            .background(
                color = Surface,
                shape = ShapeDefaults.Medium,
            )
            .padding(
                vertical = ScannerPopupDefaults.Container.verticalPadding,
                horizontal = ScannerPopupDefaults.Container.horizontalPadding,
            ),
    ) {
        Image(
            painter = painterResource(id = content.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(ScannerPopupDefaults.Image.size)
                .align(Alignment.CenterVertically),
        )

        Spacer(modifier = Modifier.width(ScannerPopupDefaults.Content.spacing))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = stringResource(id = content.titleResId),
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(ScannerPopupDefaults.Text.spacing))

            Text(
                text = stringResource(id = content.descriptionResId),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Immutable
private data class ScannerPopupContent(val imageResId: Int, val titleResId: Int, val descriptionResId: Int) {
    companion object {
        fun resolve(isPermissionDenied: Boolean): ScannerPopupContent {
            return if (isPermissionDenied) {
                ScannerPopupContent(
                    imageResId = R.drawable.scanner_popup_permission_denied,
                    titleResId = R.string.scanner_popup_permission_denied_title,
                    descriptionResId = R.string.scanner_popup_permission_denied_desc,
                )
            } else {
                ScannerPopupContent(
                    imageResId = R.drawable.scanner_popup_idle,
                    titleResId = R.string.scanner_popup_idle_title,
                    descriptionResId = R.string.scanner_popup_idle_desc,
                )
            }
        }
    }
}

private object ScannerPopupDefaults {
    object Container {
        val horizontalPadding = 16.dp
        val verticalPadding = 13.dp
    }

    object Border {
        val width = 1.5.dp
    }

    object Image {
        val size = 90.dp
    }

    object Content {
        val spacing = 12.dp
    }

    object Text {
        val spacing = 4.dp
    }
}