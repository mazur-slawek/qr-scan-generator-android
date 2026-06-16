package software.mazur.qrezzy.domain.history.repository

import kotlinx.coroutines.flow.Flow
import software.mazur.qrezzy.domain.history.model.QrHistoryItem

interface QrHistoryRepository {
    fun observeAll(): Flow<List<QrHistoryItem>>

    suspend fun save(item: QrHistoryItem): Long

    suspend fun getById(id: Long): QrHistoryItem?

    suspend fun deleteByIds(ids: List<Long>)

    suspend fun deleteAll()
}