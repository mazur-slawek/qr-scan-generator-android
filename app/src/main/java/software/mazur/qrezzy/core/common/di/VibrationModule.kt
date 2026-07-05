package software.mazur.qrezzy.core.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.core.common.vibration.AndroidVibrationService
import software.mazur.qrezzy.core.common.vibration.VibrationService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VibrationModule {
    @Binds
    @Singleton
    abstract fun bindVibrationService(impl: AndroidVibrationService): VibrationService
}