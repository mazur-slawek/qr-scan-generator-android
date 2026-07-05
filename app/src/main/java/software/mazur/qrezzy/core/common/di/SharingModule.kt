package software.mazur.qrezzy.core.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import software.mazur.qrezzy.core.common.sharing.AndroidQrSharingService
import software.mazur.qrezzy.core.common.sharing.QrSharingService

@Module
@InstallIn(SingletonComponent::class)
abstract class SharingModule {
    @Binds
    @Singleton
    abstract fun bindQrSharingService(impl: AndroidQrSharingService): QrSharingService
}
