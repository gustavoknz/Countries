package dev.gustavo.countries.feature.detail

sealed interface DetailAction {
    data class LoadDetail(val cca3: String) : DetailAction
    data object BackClicked : DetailAction
}
