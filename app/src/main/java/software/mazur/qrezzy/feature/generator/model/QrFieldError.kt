package software.mazur.qrezzy.feature.generator.model

sealed interface QrFieldError {
    data class MaxLength(val maxLength: Int) : QrFieldError
}
