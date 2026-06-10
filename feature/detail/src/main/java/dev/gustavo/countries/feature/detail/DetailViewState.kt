package dev.gustavo.countries.feature.detail

import dev.gustavo.countries.feature.detail.model.UiCountryDetail

sealed interface DetailViewState {
    data object Loading : DetailViewState
    data class Loaded(val country: UiCountryDetail) : DetailViewState
    data class Error(val message: String) : DetailViewState
}
