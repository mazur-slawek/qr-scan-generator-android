package software.mazur.qrezzy.data.history.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.data.history.mapper.toDomain
import software.mazur.qrezzy.data.history.mapper.toEntity
import software.mazur.qrezzy.domain.qr.model.QrItem
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(private val qrDao: QrDao) : QrRepository {
    override fun observeAll(): Flow<List<QrItem>> {
        return qrDao.observeAll()
            .map { entities ->
                entities.map { entity -> entity.toDomain() }
            }
    }

    override suspend fun save(item: QrItem): Long {
        return qrDao.insert(item.toEntity())
    }

    override suspend fun getById(id: Long): QrItem? {
        return qrDao.getById(id)?.toDomain()
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        qrDao.deleteByIds(ids)
    }

    override suspend fun deleteAll() {
        qrDao.deleteAll()
    }
}