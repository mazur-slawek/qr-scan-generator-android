package software.mazur.qrezzy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import software.mazur.qrezzy.data.database.dao.QrHistoryDao
import software.mazur.qrezzy.data.history.local.QrHistoryEntity

@Database(
    entities = [QrHistoryEntity::class],
    version = QrezzyDatabase.DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(QrezzyTypeConverters::class)
abstract class QrezzyDatabase : RoomDatabase() {
    abstract fun qrHistoryDao(): QrHistoryDao

    companion object {
        const val DATABASE_NAME = "qrezzy_database"
        const val DATABASE_VERSION = 1
    }
}