package software.mazur.qrezzy.core.designsystem.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import software.mazur.qrezzy.R

val QrezzyFontFamily = FontFamily(
    Font(
        resId = R.font.fredoka_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        resId = R.font.fredoka_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.fredoka_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        resId = R.font.fredoka_bold,
        weight = FontWeight.Bold,
    ),
)