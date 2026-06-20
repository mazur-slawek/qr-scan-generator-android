package software.mazur.qrezzy.core.common.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.mazur.qrezzy.core.common.sharing.QrSharingService

@EntryPoint
@InstallIn(SingletonComponent::class)
interface QrSharingEntryPoint {
    fun qrSharingService(): QrSharingService
}
