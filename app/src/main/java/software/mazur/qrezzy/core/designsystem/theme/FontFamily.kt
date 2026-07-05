package software.mazur.qrezzy.core.designsystem.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import software.mazur.qrezzy.R

val QrezzyFontFamily =
    FontFamily(
        Font(
            resId = R.font.ubuntu_sans_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.ubuntu_sans_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.ubuntu_sans_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resId = R.font.ubuntu_sans_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.ubuntu_sans_extrabold,
            weight = FontWeight.ExtraBold
        )
    )
