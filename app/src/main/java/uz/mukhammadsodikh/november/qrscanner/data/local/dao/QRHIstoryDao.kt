package uz.mukhammadsodikh.november.qrscanner.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.mukhammadsodikh.november.qrscanner.data.local.entity.QRHistoryEntity

/**
 * Room DAO - Database operations
 */
@Dao
interface QRHistoryDao {

    /**
     * Barcha QR history - Flow (real-time updates)
     */
    @Query("SELECT * FROM qr_history ORDER BY scannedAt DESC")
    fun getAllHistory(): Flow<List<QRHistoryEntity>>

    /**
     * Favorite'lar
     */
    @Query("SELECT * FROM qr_history WHERE isFavorite = 1 ORDER BY scannedAt DESC")
    fun getFavorites(): Flow<List<QRHistoryEntity>>

    /**
     * Type bo'yicha filter
     */
    @Query("SELECT * FROM qr_history WHERE type = :type ORDER BY scannedAt DESC")
    fun getHistoryByType(type: String): Flow<List<QRHistoryEntity>>

    /**
     * Search
     */
    @Query("SELECT * FROM qr_history WHERE rawContent LIKE '%' || :query || '%' ORDER BY scannedAt DESC")
    fun searchHistory(query: String): Flow<List<QRHistoryEntity>>

    /**
     * ID bo'yicha olish
     */
    @Query("SELECT * FROM qr_history WHERE id = :id")
    suspend fun getById(id: String): QRHistoryEntity?

    /**
     * Insert yoki Update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QRHistoryEntity)

    /**
     * Insert list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<QRHistoryEntity>)

    /**
     * Update
     */
    @Update
    suspend fun update(entity: QRHistoryEntity)

    /**
     * Delete
     */
    @Delete
    suspend fun delete(entity: QRHistoryEntity)

    /**
     * Delete by ID
     */
    @Query("DELETE FROM qr_history WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Delete all
     */
    @Query("DELETE FROM qr_history")
    suspend fun deleteAll()

    /**
     * Toggle favorite
     */
    @Query("UPDATE qr_history SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: String)

    /**
     * Count
     */
    @Query("SELECT COUNT(*) FROM qr_history")
    suspend fun getCount(): Int
}