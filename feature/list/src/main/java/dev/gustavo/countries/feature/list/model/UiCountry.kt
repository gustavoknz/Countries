package dev.gustavo.countries.feature.list.model

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.domain.model.Country

@Immutable
data class UiCountry(
    val cca3: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val independent: Boolean
)

fun Country.toUiModel(): UiCountry {
    return UiCountry(
        cca3 = cca3,
        commonName = commonName,
        capital = capital,
        flagUrl = flagUrl,
        region = region,
        independent = independent
    )
}
