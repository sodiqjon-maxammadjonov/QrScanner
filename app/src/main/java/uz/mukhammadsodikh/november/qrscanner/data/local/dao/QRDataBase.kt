package uz.mukhammadsodikh.november.qrscanner.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.mukhammadsodikh.november.qrscanner.data.local.dao.QRHistoryDao
import uz.mukhammadsodikh.november.qrscanner.data.local.entity.QRHistoryEntity

/**
 * Room Database
 */
@Database(
    entities = [QRHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QRDatabase : RoomDatabase() {

    abstract fun qrHistoryDao(): QRHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: QRDatabase? = null

        fun getInstance(context: Context): QRDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QRDatabase::class.java,
                    "qr_scanner_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}