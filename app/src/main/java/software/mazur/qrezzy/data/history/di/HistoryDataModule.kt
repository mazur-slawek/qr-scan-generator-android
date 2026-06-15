package software.mazur.qrezzy.data.history.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.data.history.repository.QrHistoryRepositoryImpl
import software.mazur.qrezzy.domain.history.repository.QrHistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryDataModule {
    @Binds
    @Singleton
    abstract fun bindQrHistoryRepository(impl: QrHistoryRepositoryImpl): QrHistoryRepository
}