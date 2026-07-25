package dev.gustavo.countries.feature.list.model

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.domain.model.Country

@Immutable
data class UiCountry(
    val countryCode: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val independent: Boolean,
)

fun Country.toUiModel(): UiCountry =
    UiCountry(
        countryCode = countryCode,
        commonName = commonName,
        capital = capital,
        flagUrl = flagUrl,
        region = region,
        independent = independent,
    )
