package software.mazur.qrezzy.domain.qr.repository

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.qr.model.Qr

interface QrRepository {
    fun observeAll(): Flow<List<Qr>>

    suspend fun save(item: Qr): Long

    suspend fun getById(id: Long): Qr?

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun deleteAll()
}
