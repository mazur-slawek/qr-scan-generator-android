package software.mazur.qrezzy.domain.qr.repository

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.qr.model.QrItem

interface QrRepository {
    fun observeAll(): Flow<List<QrItem>>

    suspend fun save(item: QrItem): Long

    suspend fun getById(id: Long): QrItem?

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun deleteAll()
}