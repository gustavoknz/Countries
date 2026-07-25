package dev.gustavo.countries.feature.list

sealed interface ListEvent {
    data class NavigateToDetail(
        val countryCode: String,
        val flagUrl: String,
    ) : ListEvent
}
