package software.mazur.qrezzy.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun QrezzyPopup(
    @DrawableRes imageResId: Int,
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = QrezzyPopupDefaults.Border.width,
                color = BorderLight,
                shape = ShapeDefaults.Medium,
            )
            .background(
                color = Surface,
                shape = ShapeDefaults.Medium,
            )
            .padding(
                horizontal = QrezzyPopupDefaults.Container.horizontalPadding,
                vertical = QrezzyPopupDefaults.Container.verticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(imageResId),
            contentDescription = null,
            modifier = Modifier.size(QrezzyPopupDefaults.Image.size),
        )
        Spacer(modifier = Modifier.width(QrezzyPopupDefaults.Container.contentSpacing))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(titleResId),
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(QrezzyPopupDefaults.Text.spacing))
            Text(
                text = stringResource(descriptionResId),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

private object QrezzyPopupDefaults {
    object Container {
        val horizontalPadding = 16.dp
        val verticalPadding = 13.dp
        val contentSpacing = 16.dp
    }

    object Border {
        val width = 0.5.dp
    }

    object Image {
        val size = 90.dp
    }

    object Text {
        val spacing = 4.dp
    }
}