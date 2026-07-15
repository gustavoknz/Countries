package dev.gustavo.countries.domain.model

import dev.gustavo.countries.core.common.Region

data class CountryQuery(
    val text: String? = null,
    val region: Region? = null
) {
    val sanitizedText: String? = text?.takeIf { it.isNotBlank() }
}
