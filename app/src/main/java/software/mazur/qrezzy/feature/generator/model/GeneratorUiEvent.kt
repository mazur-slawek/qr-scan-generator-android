package software.mazur.qrezzy.feature.generator.model

sealed interface GeneratorUiEvent {
    data object QrSaved : GeneratorUiEvent
    data object QrSaveFailed : GeneratorUiEvent
}