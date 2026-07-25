package dev.gustavo.countries.feature.detail

sealed interface DetailAction {
    data class LoadDetail(
        val countryCode: String,
        val flagUrl: String? = null,
    ) : DetailAction

    data object BackClicked : DetailAction

    data class BorderClicked(
        val countryCode: String,
    ) : DetailAction
}
