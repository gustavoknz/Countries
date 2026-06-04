package dev.gustavo.countries.feature.detail

sealed interface DetailEvent {
    data object NavigateBack : DetailEvent
}
