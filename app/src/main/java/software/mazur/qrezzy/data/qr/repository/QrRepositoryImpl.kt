package software.mazur.qrezzy.data.qr.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.data.qr.mapper.toDomain
import software.mazur.qrezzy.data.qr.mapper.toEntity
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Inject

class QrRepositoryImpl
@Inject
constructor(private val qrDao: QrDao) : QrRepository {
    override fun observeAll(): Flow<List<Qr>> {
        return qrDao.observeAll().map { entities -> entities.map { entity -> entity.toDomain() } }
    }

    override suspend fun save(item: Qr): Long {
        return qrDao.insert(item.toEntity())
    }

    override suspend fun getById(id: Long): Qr? {
        return qrDao.getById(id)?.toDomain()
    }

    override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
        qrDao.updateFavoriteStatus(id = id, isFavorite = isFavorite)
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        qrDao.deleteByIds(ids)
    }

    override suspend fun deleteAll() {
        qrDao.deleteAll()
    }
}
