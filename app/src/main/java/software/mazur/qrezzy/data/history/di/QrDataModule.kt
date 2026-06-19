package software.mazur.qrezzy.data.history.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.data.history.repository.QrRepositoryImpl
import software.mazur.qrezzy.domain.qr.repository.QrRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QrDataModule {
    @Binds
    @Singleton
    abstract fun bindQrHistoryRepository(impl: QrRepositoryImpl): QrRepository
}