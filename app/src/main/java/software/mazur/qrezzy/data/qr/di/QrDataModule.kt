package software.mazur.qrezzy.data.qr.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import software.mazur.qrezzy.data.qr.repository.QrRepositoryImpl
import software.mazur.qrezzy.domain.qr.repository.QrRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class QrDataModule {
    @Binds
    @Singleton
    abstract fun bindQrRepository(impl: QrRepositoryImpl): QrRepository
}
