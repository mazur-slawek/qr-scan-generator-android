package software.mazur.qrezzy.core.designsystem.extensions

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.InsertLink
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPinkDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurpleDark
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellowDark
import software.mazur.qrezzy.domain.qr.model.QrType

@Immutable
data class QrTypeUi(
    @get:StringRes val labelResId: Int,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color,
)

val QrType.ui: QrTypeUi
    get() = when (this) {
        QrType.TEXT    -> QrTypeUi(
            labelResId = R.string.qr_type_text,
            icon = Icons.Outlined.Textsms,
            containerColor = QrezzyYellowDark,
            contentColor = QrezzyYellow,
        )

        QrType.URL     -> QrTypeUi(
            labelResId = R.string.qr_type_url,
            icon = Icons.Outlined.InsertLink,
            containerColor = QrezzyMintDark,
            contentColor = QrezzyMint,
        )

        QrType.WIFI    -> QrTypeUi(
            labelResId = R.string.qr_type_wifi,
            icon = Icons.Outlined.Wifi,
            containerColor = QrezzyPurpleDark,
            contentColor = QrezzyPurple,
        )

        QrType.CONTACT -> QrTypeUi(
            labelResId = R.string.qr_type_contact,
            icon = Icons.Outlined.AccountCircle,
            containerColor = QrezzyYellowDark,
            contentColor = QrezzyYellow,
        )

        QrType.EMAIL   -> QrTypeUi(
            labelResId = R.string.qr_type_email,
            icon = Icons.Outlined.AlternateEmail,
            containerColor = QrezzyPinkDark,
            contentColor = QrezzyPink,
        )

        QrType.PHONE   -> QrTypeUi(
            labelResId = R.string.qr_type_phone,
            icon = Icons.Outlined.Smartphone,
            containerColor = QrezzyPurpleDark,
            contentColor = QrezzyPurple,
        )
    }