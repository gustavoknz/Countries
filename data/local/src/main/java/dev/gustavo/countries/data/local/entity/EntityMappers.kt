package dev.gustavo.countries.data.local.entity

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

fun CountryEntity.toDomain(): Country = Country(
    cca3 = cca3,
    commonName = commonName,
    capital = capital,
    flagUrl = flagUrl,
    region = region
)

fun Country.toEntity(): CountryEntity = CountryEntity(
    cca3 = cca3,
    commonName = commonName,
    capital = capital,
    flagUrl = flagUrl,
    region = region
)

fun CountryDetailEntity.toDomain(): CountryDetail = CountryDetail(
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
    currencies = currencies
)

fun CountryDetail.toEntity(): CountryDetailEntity = CountryDetailEntity(
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
    currencies = currencies
)
