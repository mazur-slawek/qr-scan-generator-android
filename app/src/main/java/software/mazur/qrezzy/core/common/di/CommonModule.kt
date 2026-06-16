package software.mazur.qrezzy.core.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.core.common.time.SystemTimeProvider
import software.mazur.qrezzy.core.common.time.TimeProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {
    @Binds
    @Singleton
    abstract fun bindTimeProvider(
        impl: SystemTimeProvider,
    ): TimeProvider
}