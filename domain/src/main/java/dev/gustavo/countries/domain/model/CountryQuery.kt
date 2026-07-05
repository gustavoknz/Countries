package dev.gustavo.countries.domain.model

data class CountryQuery(
    val text: String? = null,
    val region: String? = null
) {
    val sanitizedText: String? = text?.takeIf { it.isNotBlank() }
}
