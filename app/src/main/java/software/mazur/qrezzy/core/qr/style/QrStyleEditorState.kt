package software.mazur.qrezzy.core.qr.style

import androidx.compose.runtime.Immutable
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

@Immutable
data class QrStyleEditorState(
    val appliedStyle: QrStyle = QrStyle(),
    val draftStyle: QrStyle = QrStyle(),
    val isDialogVisible: Boolean = false,
) {
    fun open(): QrStyleEditorState =
        copy(
            draftStyle = appliedStyle,
            isDialogVisible = true,
        )

    fun dismiss(): QrStyleEditorState =
        copy(isDialogVisible = false)

    fun resetDraft(): QrStyleEditorState =
        copy(draftStyle = QrStyle())

    fun applyDraft(): QrStyleEditorState =
        copy(
            appliedStyle = draftStyle,
            isDialogVisible = false,
        )

    fun updateQrColor(color: Long): QrStyleEditorState =
        copy(draftStyle = draftStyle.copy(qrColor = color))

    fun updateBackgroundColor(color: Long): QrStyleEditorState =
        copy(draftStyle = draftStyle.copy(backgroundColor = color))

    fun updatePatternStyle(patternStyle: QrPatternStyle): QrStyleEditorState =
        copy(draftStyle = draftStyle.copy(patternStyle = patternStyle))

    fun updateErrorCorrection(errorCorrection: QrErrorCorrection): QrStyleEditorState =
        copy(draftStyle = draftStyle.copy(errorCorrection = errorCorrection))
}