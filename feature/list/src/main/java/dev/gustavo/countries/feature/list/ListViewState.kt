package dev.gustavo.countries.feature.list

import dev.gustavo.countries.domain.model.Country
import kotlinx.collections.immutable.ImmutableList

sealed interface ListViewState {
    data object Loading : ListViewState
    data class Loaded(val countries: ImmutableList<Country>) : ListViewState
    data class Error(val message: String) : ListViewState
}
