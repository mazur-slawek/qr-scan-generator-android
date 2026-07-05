package software.mazur.qrezzy.data.qr.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle
import software.mazur.qrezzy.domain.qr.model.style.QrStyleDefaults

@Entity(tableName = QrEntity.TABLE_NAME)
data class QrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val source: QrSource,
    val type: QrType,
    val content: String,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val qrColor: Long = QrStyleDefaults.QR_COLOR,
    val backgroundColor: Long = QrStyleDefaults.BACKGROUND_COLOR,
    val patternStyle: QrPatternStyle = QrPatternStyle.SQUARE,
    val errorCorrection: QrErrorCorrection = QrErrorCorrection.MEDIUM
) {
    companion object {
        const val TABLE_NAME = "qr"
    }
}
