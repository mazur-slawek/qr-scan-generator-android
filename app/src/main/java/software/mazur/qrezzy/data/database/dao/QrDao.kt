package software.mazur.qrezzy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.data.history.local.QrEntity

@Dao
interface QrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QrEntity): Long

    @Query("""SELECT * FROM qr_history ORDER BY createdAt DESC""")
    fun observeAll(): Flow<List<QrEntity>>

    @Query("""SELECT * FROM qr_history WHERE id = :id LIMIT 1""")
    suspend fun getById(id: Long): QrEntity?

    @Query("""DELETE FROM qr_history WHERE id IN (:ids)""")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM qr_history")
    suspend fun deleteAll()
}