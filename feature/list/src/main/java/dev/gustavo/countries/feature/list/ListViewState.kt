package dev.gustavo.countries.feature.list

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface ListViewState {
    val isOffline: Boolean

    data class Loading(
        override val isOffline: Boolean = false
    ) : ListViewState

    data class Loaded(
        val countries: ImmutableList<UiCountry>,
        val isRefreshing: Boolean = false,
        override val isOffline: Boolean = false
    ) : ListViewState

    data class Error(
        val message: String,
        val isRefreshing: Boolean = false,
        override val isOffline: Boolean = false
    ) : ListViewState
}
