package software.mazur.qrezzy.feature.generator.domain

import software.mazur.qrezzy.feature.generator.mapper.QrInputContentMapper
import software.mazur.qrezzy.feature.generator.model.QrInput
import javax.inject.Inject

class GenerateQrContentUseCase @Inject constructor() {
    operator fun invoke(qrInput: QrInput): String {
        return QrInputContentMapper.mapToQrContent(qrInput)
    }
}