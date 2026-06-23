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
import software.mazur.qrezzy.data.database.dao.QrDao
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
            .addMigrations(MIGRATION_1_2)
            .build()

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""ALTER TABLE qr ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0 """.trimIndent())
        }
    }

    @Provides
    fun provideQrDao(database: QrezzyDatabase): QrDao = database.qrDao()
}
