package dev.gustavo.countries.feature.list

sealed interface ListAction {
    data object LoadCountries : ListAction
    data class SearchQueryChanged(val query: String) : ListAction
    data object SearchTriggered : ListAction
    data object Refresh : ListAction
    data class CountryClicked(val cca3: String) : ListAction
}
