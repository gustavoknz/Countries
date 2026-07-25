package dev.gustavo.countries.feature.detail

sealed interface DetailEvent {
    data object NavigateBack : DetailEvent

    data class NavigateToDetail(
        val countryCode: String,
    ) : DetailEvent
}
