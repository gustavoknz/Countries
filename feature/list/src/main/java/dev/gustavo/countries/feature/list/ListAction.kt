package dev.gustavo.countries.feature.list

import dev.gustavo.countries.core.common.Region

sealed interface ListAction {
    data class SearchQueryChanged(val query: String) : ListAction
    data class RegionSelected(val region: Region?) : ListAction
    data class CountryClicked(val cca3: String, val flagUrl: String) : ListAction
}
