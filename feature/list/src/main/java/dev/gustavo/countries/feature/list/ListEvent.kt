package dev.gustavo.countries.feature.list

sealed interface ListEvent {
    data class NavigateToDetail(val cca3: String) : ListEvent
}
