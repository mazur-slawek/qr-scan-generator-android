package software.mazur.qrezzy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.data.qr.local.QrEntity
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle

@Dao
interface QrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QrEntity): Long

    @Query("""SELECT * FROM qr ORDER BY createdAt DESC""")
    fun observeAll(): Flow<List<QrEntity>>

    @Query("""SELECT * FROM qr WHERE id = :id LIMIT 1""")
    suspend fun getById(id: Long): QrEntity?

    @Query("""UPDATE qr SET isFavorite = :isFavorite WHERE id = :id""")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("""UPDATE qr SET
            qrColor = :qrColor, backgroundColor = :backgroundColor,
            patternStyle = :patternStyle, errorCorrection = :errorCorrection WHERE id = :id""")
    suspend fun updateStyle(
        id: Long,
        qrColor: Long,
        backgroundColor: Long,
        patternStyle: QrPatternStyle,
        errorCorrection: QrErrorCorrection,
    )

    @Query("""DELETE FROM qr WHERE id IN (:ids)""")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM qr")
    suspend fun deleteAll()
}
