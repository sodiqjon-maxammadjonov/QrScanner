package uz.mukhammadsodikh.november.qrscanner.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.mukhammadsodikh.november.qrscanner.data.local.dao.QRHistoryDao
import uz.mukhammadsodikh.november.qrscanner.data.local.entity.QRHistoryEntity
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode

/**
 * History Repository
 */
class HistoryRepository(private val dao: QRHistoryDao) {

    /**
     * Barcha history - Flow
     */
    fun getAllHistory(): Flow<List<QRCode>> {
        return dao.getAllHistory().map { entities ->
            entities.map { it.toQRCode() }
        }
    }

    /**
     * Favorite'lar
     */
    fun getFavorites(): Flow<List<QRCode>> {
        return dao.getFavorites().map { entities ->
            entities.map { it.toQRCode() }
        }
    }

    /**
     * Type bo'yicha filter
     */
    fun getHistoryByType(type: String): Flow<List<QRCode>> {
        return dao.getHistoryByType(type).map { entities ->
            entities.map { it.toQRCode() }
        }
    }

    /**
     * Search
     */
    fun searchHistory(query: String): Flow<List<QRCode>> {
        return dao.searchHistory(query).map { entities ->
            entities.map { it.toQRCode() }
        }
    }

    /**
     * ID bo'yicha olish
     */
    suspend fun getById(id: String): QRCode? {
        return dao.getById(id)?.toQRCode()
    }

    /**
     * QR Code saqlash
     */
    suspend fun saveQRCode(qrCode: QRCode, format: String = "QR_CODE") {
        val entity = QRHistoryEntity.fromQRCode(qrCode, format)
        dao.insert(entity)
    }

    /**
     * Delete
     */
    suspend fun delete(qrCode: QRCode) {
        val entity = QRHistoryEntity.fromQRCode(qrCode)
        dao.delete(entity)
    }

    /**
     * Delete by ID
     */
    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    /**
     * Delete all
     */
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    /**
     * Toggle favorite
     */
    suspend fun toggleFavorite(id: String) {
        dao.toggleFavorite(id)
    }

    /**
     * Count
     */
    suspend fun getCount(): Int {
        return dao.getCount()
    }
}