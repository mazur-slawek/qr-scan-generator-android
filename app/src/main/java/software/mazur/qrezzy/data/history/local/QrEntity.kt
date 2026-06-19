package software.mazur.qrezzy.data.history.local

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

    /**
     * Krótki tekst wyświetlany na liście historii.
     */
    val title: String,

    /**
     * Pełna zawartość zakodowana w QR.
     */
    val content: String,

    /**
     * Oryginalne dane formularza zapisane jako JSON.
     *
     * Dzięki temu możliwe jest późniejsze
     * odtworzenie formularza i edycja QR.
     */
    val payloadJson: String?,

    /**
     * Timestamp utworzenia wpisu.
     */
    val createdAt: Long,
) {
    companion object {
        const val TABLE_NAME = "qr_history"
    }
}