package software.mazur.qrezzy.feature.generator.presentation.model

sealed interface GeneratorUiEvent {
    data object QrSaved : GeneratorUiEvent
}