package dev.gustavo.countries.feature.detail.model

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.domain.model.CountryDetail

@Immutable
data class UiCountryDetail(
    val cca3: String,
    val commonName: String,
    val officialName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val subregion: String,
    val languages: List<String>,
    val population: Long,
    val borders: List<String>,
    val currencies: List<String>,
)

fun CountryDetail.toUiModel(): UiCountryDetail {
    return UiCountryDetail(
        cca3 = cca3,
        commonName = commonName,
        officialName = officialName,
        capital = capital,
        flagUrl = flagUrl,
        region = region,
        subregion = subregion,
        languages = languages,
        population = population,
        borders = borders,
        currencies = currencies,
    )
}
