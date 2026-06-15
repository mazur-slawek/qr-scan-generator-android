package software.mazur.qrezzy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.data.history.local.QrHistoryEntity
import software.mazur.qrezzy.data.history.local.QrHistorySource

@Dao
interface QrHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QrHistoryEntity): Long

    @Query("""SELECT * FROM qr_history ORDER BY createdAt DESC""")
    fun observeAll(): Flow<List<QrHistoryEntity>>

    @Query("""SELECT * FROM qr_history WHERE source = :source ORDER BY createdAt DESC""")
    fun observeBySource(source: QrHistorySource): Flow<List<QrHistoryEntity>>

    @Query("""SELECT * FROM qr_history WHERE id = :id LIMIT 1""")
    suspend fun getById(id: Long): QrHistoryEntity?

    @Query("""DELETE FROM qr_history WHERE id IN (:ids)""")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM qr_history")
    suspend fun deleteAll()

    @Query("""UPDATE qr_history SET isFavorite = :isFavorite WHERE id = :id""")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
}