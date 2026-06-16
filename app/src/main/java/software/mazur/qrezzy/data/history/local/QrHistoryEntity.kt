package software.mazur.qrezzy.data.history.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import software.mazur.qrezzy.domain.history.model.QrHistorySource
import software.mazur.qrezzy.domain.history.model.QrHistoryType

@Entity(tableName = QrHistoryEntity.TABLE_NAME)
data class QrHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val source: QrHistorySource,
    val type: QrHistoryType,

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

    /**
     * Oznaczenie jako ulubione.
     */
    val isFavorite: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "qr_history"
    }
}