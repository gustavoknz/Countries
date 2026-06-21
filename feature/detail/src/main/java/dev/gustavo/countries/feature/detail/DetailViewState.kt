package dev.gustavo.countries.feature.detail

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail

@Immutable
sealed interface DetailViewState {
    data class Loading(val cca3: String? = null, val flagUrl: String? = null) : DetailViewState
    data class Loaded(val country: UiCountryDetail) : DetailViewState
    data class Error(val message: UiText, val countryCode: String? = null) : DetailViewState
}
