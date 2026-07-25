package dev.gustavo.countries.core.testing

import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.remote.model.CapitalRemote
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.CurrencyRemote
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.LanguageRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

object TestData {
    const val COUNTRY_CODE_BRA = "BRA"
    const val COUNTRY_NAME_BRA = "Brazil"
    const val FLAG_URL_BRA = "https://flagcdn.com/w320/br.png"

    fun createCountry(
        countryCode: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA,
    ) = Country(
        countryCode = countryCode,
        commonName = commonName,
        capital = "Brasília",
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        independent = true,
    )

    fun createCountryDetail(
        countryCode: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA,
        population: Long = 215_000_000L,
    ) = CountryDetail(
        countryCode = countryCode,
        commonName = commonName,
        officialName = "Federative Republic of Brazil",
        capital = "Brasília",
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        subregion = "South America",
        languages = listOf("Portuguese"),
        population = population,
        borders = listOf("ARG", "URY"),
        currencies = listOf("Brazilian Real"),
        independent = true,
    )

    fun createCountryEntity(
        countryCode: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA,
    ) = CountryEntity(
        countryCode = countryCode,
        commonName = commonName,
        capital = "Brasília",
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        independent = true,
    )

    fun createCountryDetailEntity(
        countryCode: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA,
        population: Long = 215_000_000L,
    ) = CountryDetailEntity(
        countryCode = countryCode,
        commonName = commonName,
        officialName = "Federative Republic of Brazil",
        capital = "Brasília",
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        subregion = "South America",
        languages = listOf("Portuguese"),
        population = population,
        borders = listOf("ARG", "URY"),
        currencies = listOf("Brazilian Real"),
        independent = true,
    )

    fun createCountryRemote(countryCode: String = COUNTRY_CODE_BRA) =
        CountryRemote(
            codes = CodesRemote(alpha3 = countryCode),
            names = NameRemote(common = COUNTRY_NAME_BRA, official = "Federative Republic of Brazil"),
            capitals = listOf(CapitalRemote(name = "Brasília")),
            flag = FlagRemote(png = FLAG_URL_BRA),
            region = "Americas",
            subregion = "South America",
            languages = listOf(LanguageRemote(name = "Portuguese")),
            population = 215_000_000L,
            borders = listOf("ARG", "URY"),
            currencies = listOf(CurrencyRemote(name = "Brazilian Real")),
            classification = ClassificationRemote(dependency = false),
        )
}
