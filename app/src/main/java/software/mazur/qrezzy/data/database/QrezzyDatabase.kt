package software.mazur.qrezzy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.data.history.local.QrEntity

@Database(
    entities = [QrEntity::class],
    version = QrezzyDatabase.DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(QrezzyTypeConverters::class)
abstract class QrezzyDatabase : RoomDatabase() {
    abstract fun qrHistoryDao(): QrDao

    companion object {
        const val DATABASE_NAME = "qrezzy_database"
        const val DATABASE_VERSION = 1
    }
}