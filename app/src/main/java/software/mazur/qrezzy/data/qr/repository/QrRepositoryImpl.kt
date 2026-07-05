package software.mazur.qrezzy.data.qr.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.data.qr.mapper.toDomain
import software.mazur.qrezzy.data.qr.mapper.toEntity
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.repository.QrRepository

class QrRepositoryImpl
@Inject
constructor(private val qrDao: QrDao) : QrRepository {
    override fun observeAll(): Flow<List<Qr>> = qrDao.observeAll().map { entities -> entities.map { entity -> entity.toDomain() } }

    override suspend fun save(item: Qr): Long = qrDao.insert(item.toEntity())

    override suspend fun getById(id: Long): Qr? = qrDao.getById(id)?.toDomain()

    override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
        qrDao.updateFavoriteStatus(id = id, isFavorite = isFavorite)
    }

    override suspend fun updateStyle(id: Long, style: QrStyle) {
        qrDao.updateStyle(
            id = id,
            qrColor = style.qrColor,
            backgroundColor = style.backgroundColor,
            patternStyle = style.patternStyle,
            errorCorrection = style.errorCorrection
        )
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        qrDao.deleteByIds(ids)
    }

    override suspend fun getCount(): Int = qrDao.getCount()

    override suspend fun getLatestCreatedAt(): Long? = qrDao.getLatestCreatedAt()

    override suspend fun deleteAll() {
        qrDao.deleteAll()
    }
}
