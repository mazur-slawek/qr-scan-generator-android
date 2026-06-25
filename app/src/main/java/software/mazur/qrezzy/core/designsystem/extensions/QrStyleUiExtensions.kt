package software.mazur.qrezzy.core.designsystem.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Long.toComposeColor(): Color = Color(this)

fun Color.toArgbLong(): Long = toArgb().toLong()
