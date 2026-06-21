package dev.gustavo.countries.feature.detail

sealed interface DetailAction {
    data class LoadDetail(val cca3: String, val flagUrl: String? = null) : DetailAction
    data object BackClicked : DetailAction
    data class BorderClicked(val cca3: String) : DetailAction
}
