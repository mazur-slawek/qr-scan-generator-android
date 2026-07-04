package software.mazur.qrezzy.data.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.data.database.QrezzyDatabase
import software.mazur.qrezzy.data.database.dao.AppSettingsDao
import software.mazur.qrezzy.data.database.dao.QrDao
import software.mazur.qrezzy.domain.qr.model.style.QrStyleDefaults
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideQrezzyDatabase(@ApplicationContext context: Context): QrezzyDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = QrezzyDatabase::class.java,
                name = QrezzyDatabase.DATABASE_NAME,
            )
//            .fallbackToDestructiveMigration(dropAllTables = true)
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4
            )
            .build()

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""ALTER TABLE qr ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0 """.trimIndent())
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """ALTER TABLE qr ADD COLUMN qrColor INTEGER NOT NULL DEFAULT ${QrStyleDefaults.QR_COLOR}""".trimIndent()
            )

            db.execSQL(
                """ALTER TABLE qr ADD COLUMN backgroundColor INTEGER NOT NULL DEFAULT ${QrStyleDefaults.BACKGROUND_COLOR}""".trimIndent()
            )

            db.execSQL(
                """ALTER TABLE qr ADD COLUMN patternStyle TEXT NOT NULL DEFAULT 'SQUARE'""".trimIndent()
            )

            db.execSQL(
                """ALTER TABLE qr ADD COLUMN errorCorrection TEXT NOT NULL DEFAULT 'MEDIUM'""".trimIndent()
            )
        }
    }
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""CREATE TABLE IF NOT EXISTS app_settings (
                id INTEGER NOT NULL PRIMARY KEY,
                onboardingCompleted INTEGER NOT NULL DEFAULT 0,
                language TEXT NOT NULL DEFAULT 'SYSTEM',
                theme TEXT NOT NULL DEFAULT 'SYSTEM',
                autoSaveScans INTEGER NOT NULL DEFAULT 0,
                vibrationEnabled INTEGER NOT NULL DEFAULT 1,
                historyLimit TEXT NOT NULL DEFAULT 'ITEMS_200')""".trimIndent())
            db.execSQL("""INSERT OR IGNORE INTO app_settings (
                id, onboardingCompleted, language, theme, autoSaveScans, vibrationEnabled, historyLimit) VALUES (
                1, 0, 'SYSTEM', 'SYSTEM', 0, 1, 'ITEMS_200')""".trimIndent()
            )
        }
    }

    @Provides
    fun provideQrDao(database: QrezzyDatabase): QrDao = database.qrDao()

    @Provides
    fun provideAppSettingsDao(database: QrezzyDatabase): AppSettingsDao = database.appSettingsDao()
}
