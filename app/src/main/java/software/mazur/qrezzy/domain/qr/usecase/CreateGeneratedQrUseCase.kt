package software.mazur.qrezzy.domain.qr.usecase

import software.mazur.qrezzy.core.common.time.TimeProvider
import software.mazur.qrezzy.domain.qr.model.Qr
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import javax.inject.Inject

class CreateGeneratedQrUseCase
    @Inject
    constructor(
        private val timeProvider: TimeProvider,
    ) {
        operator fun invoke(
            type: QrType,
            content: String,
        ): Qr =
            Qr(
                type = type,
                source = QrSource.GENERATED,
                content = content.trim(),
                createdAt = timeProvider.nowMillis(),
            )
    }
