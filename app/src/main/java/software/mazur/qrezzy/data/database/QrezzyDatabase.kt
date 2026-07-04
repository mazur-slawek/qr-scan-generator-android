package software.mazur.qrezzy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import software.mazur.qrezzy.data.database.dao.AppSettingsDao
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.data.qr.local.QrEntity
import software.mazur.qrezzy.data.settings.local.AppSettingsEntity

@Database(
    entities = [
        QrEntity::class,
        AppSettingsEntity::class,
    ],
    version = QrezzyDatabase.DATABASE_VERSION,
    exportSchema = true,
)
@TypeConverters(QrezzyTypeConverters::class)
abstract class QrezzyDatabase : RoomDatabase() {
    abstract fun qrDao(): QrDao
    abstract fun appSettingsDao(): AppSettingsDao
    
    companion object {
        const val DATABASE_NAME = "qrezzy_database"
        const val DATABASE_VERSION = 4
    }
}
