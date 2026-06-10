package dev.gustavo.countries.feature.list

import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.collections.immutable.ImmutableList

sealed interface ListViewState {
    data object Loading : ListViewState
    data class Loaded(val countries: ImmutableList<UiCountry>) : ListViewState
    data class Error(val message: String) : ListViewState
}
