package software.mazur.qrezzy.feature.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.BorderLight
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.Surface
import software.mazur.qrezzy.core.designsystem.theme.TextPrimary
import software.mazur.qrezzy.core.designsystem.theme.TextSecondary

@Composable
fun HistorySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    enabled: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused && enabled) QrezzyPurple else BorderLight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else HistorySearchBarDefaults.DISABLED_ALPHA)
            .height(HistorySearchBarDefaults.height)
            .border(
                width = HistorySearchBarDefaults.borderWidth,
                color = borderColor,
                shape = ShapeDefaults.Medium,
            )
            .background(color = Surface, shape = ShapeDefaults.Medium)
            .padding(
                start = HistorySearchBarDefaults.horizontalPadding,
                end = HistorySearchBarDefaults.horizontalPadding / 2
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Search, contentDescription = null, tint = TextSecondary)
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            enabled = enabled,
            singleLine = true,
            cursorBrush = SolidColor(QrezzyMint),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .weight(HistorySearchBarDefaults.TEXT_FIELD_WEIGHT)
                .padding(horizontal = HistorySearchBarDefaults.textHorizontalPadding)
                .onFocusChanged { state ->
                    isFocused = state.isFocused
                },
            decorationBox = { innerTextField ->
                if (query.isBlank()) {
                    Text(
                        text = stringResource(R.string.history_search_placeholder),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerTextField()
            },
        )

        if (query.isNotBlank()) {
            IconButton(onClick = onClearClick) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = null, tint = TextSecondary)
            }
        }
    }

}

private object HistorySearchBarDefaults {
    val height = 48.dp
    val borderWidth = 1.5.dp
    val horizontalPadding = 12.dp
    val textHorizontalPadding = 10.dp
    const val DISABLED_ALPHA = 0.4f
    const val TEXT_FIELD_WEIGHT = 1f
}