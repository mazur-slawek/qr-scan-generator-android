package software.mazur.qrezzy.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.core.database.QrezzyDatabase
import software.mazur.qrezzy.core.database.dao.QrHistoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideQrezzyDatabase(@ApplicationContext context: Context): QrezzyDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = QrezzyDatabase::class.java,
            name = QrezzyDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideQrHistoryDao(database: QrezzyDatabase): QrHistoryDao {
        return database.qrHistoryDao()
    }
}