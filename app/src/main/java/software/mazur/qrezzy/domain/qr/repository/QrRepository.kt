package software.mazur.qrezzy.domain.qr.repository

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.style.QrStyle

interface QrRepository {
    fun observeAll(): Flow<List<Qr>>

    suspend fun save(item: Qr): Long

    suspend fun getById(id: Long): Qr?

    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    suspend fun updateStyle(id: Long, style: QrStyle)

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun getCount(): Int

    suspend fun getLatestCreatedAt(): Long?
    
    suspend fun deleteAll()
}
