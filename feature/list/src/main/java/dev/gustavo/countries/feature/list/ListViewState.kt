package dev.gustavo.countries.feature.list

import dev.gustavo.countries.domain.model.Country
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface ListViewState {
    data object Loading : ListViewState

    data class Loaded(
        val countries: ImmutableList<Country>,
        val searchQuery: String = "",
    ) : ListViewState

    data class Error(val message: String) : ListViewState
}
