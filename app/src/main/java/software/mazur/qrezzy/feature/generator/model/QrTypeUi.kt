package software.mazur.qrezzy.feature.generator.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.InsertLink
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

val QrType.label: String
    get() = when (this) {
        is QrType.Text    -> "Text"
        is QrType.Url     -> "URL"
        is QrType.Wifi    -> "Wi-Fi"
        is QrType.Contact -> "Contact"
        is QrType.Email   -> "Email"
        is QrType.Phone   -> "Phone"
    }
val QrType.icon: ImageVector
    get() = when (this) {
        is QrType.Text    -> Icons.Outlined.Textsms
        is QrType.Url     -> Icons.Outlined.InsertLink
        is QrType.Wifi    -> Icons.Outlined.Wifi
        is QrType.Contact -> Icons.Outlined.AccountCircle
        is QrType.Email   -> Icons.Outlined.AlternateEmail
        is QrType.Phone   -> Icons.Outlined.Smartphone
    }