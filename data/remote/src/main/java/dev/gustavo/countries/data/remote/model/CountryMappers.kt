package dev.gustavo.countries.data.remote.model

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

fun CountryRemote.toCountry(): Country = Country(
    cca3 = cca3.orEmpty(),
    commonName = name?.common.orEmpty(),
    capital = capital?.firstOrNull().orEmpty(),
    flagUrl = flags?.png.orEmpty(),
    region = region.orEmpty()
)

fun CountryRemote.toCountryDetail(): CountryDetail = CountryDetail(
    cca3 = cca3.orEmpty(),
    commonName = name?.common.orEmpty(),
    officialName = name?.official.orEmpty(),
    capital = capital?.firstOrNull().orEmpty(),
    flagUrl = flags?.png.orEmpty(),
    region = region.orEmpty(),
    subregion = subregion.orEmpty(),
    languages = languages?.values?.toList() ?: emptyList(),
    population = population ?: 0L,
    borders = borders ?: emptyList(),
    currencies = currencies?.values?.mapNotNull { it.name } ?: emptyList()
)
