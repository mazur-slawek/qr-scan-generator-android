package software.mazur.qrezzy.data.history.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.mazur.qrezzy.core.database.dao.QrHistoryDao
import software.mazur.qrezzy.data.history.local.QrHistorySource
import software.mazur.qrezzy.data.history.mapper.toDomain
import software.mazur.qrezzy.data.history.mapper.toEntity
import software.mazur.qrezzy.domain.history.model.QrHistoryItem
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Inject

class QrHistoryRepositoryImpl @Inject constructor(private val qrHistoryDao: QrHistoryDao) : QrHistoryRepository {
    override fun observeAll(): Flow<List<QrHistoryItem>> {
        return qrHistoryDao.observeAll()
            .map { entities ->
                entities.map { entity -> entity.toDomain() }
            }
    }

    override fun observeBySource(source: QrHistorySource): Flow<List<QrHistoryItem>> {
        return qrHistoryDao.observeBySource(source)
            .map { entities ->
                entities.map { entity -> entity.toDomain() }
            }
    }

    override suspend fun save(item: QrHistoryItem): Long {
        return qrHistoryDao.insert(item.toEntity())
    }

    override suspend fun getById(id: Long): QrHistoryItem? {
        return qrHistoryDao.getById(id)?.toDomain()
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        qrHistoryDao.deleteByIds(ids)
    }

    override suspend fun deleteAll() {
        qrHistoryDao.deleteAll()
    }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        qrHistoryDao.updateFavorite(id = id, isFavorite = isFavorite)
    }
}