package software.mazur.qrezzy.data.qr.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType

@Entity(tableName = QrEntity.TABLE_NAME)
data class QrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val source: QrSource,
    val type: QrType,
    val content: String,
    val createdAt: Long,
) {
    companion object {
        const val TABLE_NAME = "qr"
    }
}
