package software.mazur.qrezzy.core.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import software.mazur.qrezzy.core.common.crash.CrashReporter
import software.mazur.qrezzy.core.common.crash.FirebaseCrashReporter

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashReporterModule {
    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: FirebaseCrashReporter): CrashReporter
}
