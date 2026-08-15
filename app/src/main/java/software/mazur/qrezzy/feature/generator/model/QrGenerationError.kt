package software.mazur.qrezzy.feature.generator.model

sealed interface QrGenerationError {
    data object CannotEncode : QrGenerationError
    data object Unknown : QrGenerationError
}
