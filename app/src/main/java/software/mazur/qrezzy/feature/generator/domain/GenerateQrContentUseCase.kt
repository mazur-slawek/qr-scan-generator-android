package software.mazur.qrezzy.feature.generator.domain

import software.mazur.qrezzy.feature.generator.mapper.QrTypeMapper
import software.mazur.qrezzy.feature.generator.model.QrType
import javax.inject.Inject

class GenerateQrContentUseCase @Inject constructor() {
    operator fun invoke(qrType: QrType): String {
        return QrTypeMapper.mapToQrContent(qrType)
    }
}