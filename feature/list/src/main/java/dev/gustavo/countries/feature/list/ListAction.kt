package dev.gustavo.countries.feature.list

sealed interface ListAction {
    data class SearchQueryChanged(val query: String) : ListAction
    data class CountryClicked(val cca3: String, val flagUrl: String) : ListAction
}
