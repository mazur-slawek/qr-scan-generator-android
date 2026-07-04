package software.mazur.qrezzy.data.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.data.settings.repository.AppSettingsRepositoryImpl
import software.mazur.qrezzy.domain.settings.repository.AppSettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppSettingsDataModule {
    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository
}