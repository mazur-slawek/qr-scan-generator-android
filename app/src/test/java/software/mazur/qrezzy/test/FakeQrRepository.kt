package software.mazur.qrezzy.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.style.QrStyle
import software.mazur.qrezzy.domain.qr.repository.QrRepository

open class FakeQrRepository(initialItems: List<Qr> = emptyList()) : QrRepository {
    private val items = MutableStateFlow(initialItems)
    var savedItems = mutableListOf<Qr>()
    var deletedIds: List<Long> = emptyList()
    var deletedAll = false
    var updatedFavorite: Pair<Long, Boolean>? = null
    var updatedStyle: Pair<Long, QrStyle>? = null

    override fun observeAll(): Flow<List<Qr>> = items
    fun observeAllCurrent() = items.value

    override suspend fun save(item: Qr): Long {
        val id = item.id.takeIf { it != 0L } ?: ((items.value.maxOfOrNull { it.id } ?: 0L) + 1L)
        val saved = item.copy(id = id)
        savedItems.add(saved)
        items.value = listOf(saved) + items.value
        return id
    }

    override suspend fun getById(id: Long): Qr? = items.value.firstOrNull { it.id == id }

    override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
        updatedFavorite = id to isFavorite
        items.value = items.value.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it }
    }

    override suspend fun updateStyle(id: Long, style: QrStyle) {
        updatedStyle = id to style
        items.value = items.value.map { if (it.id == id) it.copy(style = style) else it }
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        deletedIds = ids
        items.value = items.value.filterNot { it.id in ids }
    }

    override suspend fun getCount(): Int = items.value.size

    override suspend fun getLatestCreatedAt(): Long? = items.value.maxOfOrNull { it.createdAt }

    override suspend fun deleteAll() {
        deletedAll = true
        items.value = emptyList()
    }
}
