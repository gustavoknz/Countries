package dev.gustavo.countries.data.remote.model

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

fun CountryRemote.toDomain(): Country = Country(
    cca3 = codes?.alpha3.orEmpty(),
    commonName = names?.common.orEmpty(),
    capital = capitals?.firstOrNull()?.name.orEmpty(),
    flagUrl = flag?.png.orEmpty(),
    region = region.orEmpty(),
    independent = classification?.dependency != true
)

fun CountryRemote.toDetailDomain(): CountryDetail = CountryDetail(
    cca3 = codes?.alpha3.orEmpty(),
    commonName = names?.common.orEmpty(),
    officialName = names?.official.orEmpty(),
    capital = capitals?.firstOrNull()?.name.orEmpty(),
    flagUrl = flag?.png.orEmpty(),
    region = region.orEmpty(),
    subregion = subregion.orEmpty(),
    languages = languages?.mapNotNull { it.name } ?: emptyList(),
    population = population ?: 0L,
    borders = borders ?: emptyList(),
    currencies = currencies?.mapNotNull { it.name } ?: emptyList(),
    independent = classification?.dependency != true
)
