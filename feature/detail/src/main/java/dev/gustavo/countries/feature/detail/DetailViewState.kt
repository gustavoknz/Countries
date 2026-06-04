package dev.gustavo.countries.feature.detail

import dev.gustavo.countries.domain.model.CountryDetail

sealed interface DetailViewState {
    data object Loading : DetailViewState
    data class Loaded(val country: CountryDetail) : DetailViewState
    data class Error(val message: String) : DetailViewState
}
