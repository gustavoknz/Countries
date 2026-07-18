package dev.gustavo.countries.data.remote.model

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

fun CountryRemote.toDomain(): Country = Country(
    cca3 = alpha3,
    commonName = commonName,
    capital = capitalName,
    flagUrl = flagUrl,
    region = region.orEmpty(),
    independent = isIndependent
)

fun CountryRemote.toDetailDomain(): CountryDetail = CountryDetail(
    cca3 = alpha3,
    commonName = commonName,
    officialName = names?.official.orEmpty(),
    capital = capitalName,
    flagUrl = flagUrl,
    region = region.orEmpty(),
    subregion = subregion.orEmpty(),
    languages = languages?.mapNotNull { it.name } ?: emptyList(),
    population = population ?: 0L,
    borders = borders ?: emptyList(),
    currencies = currencies?.mapNotNull { it.name } ?: emptyList(),
    independent = isIndependent
)

private val CountryRemote.alpha3: String
    get() = codes?.alpha3.orEmpty()

private val CountryRemote.commonName: String
    get() = names?.common.orEmpty()

private val CountryRemote.capitalName: String
    get() = capitals?.firstOrNull()?.name.orEmpty()

private val CountryRemote.flagUrl: String
    get() = flag?.png.orEmpty()

private val CountryRemote.isIndependent: Boolean
    get() = classification?.dependency != true
