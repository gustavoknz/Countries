package dev.gustavo.countries.core.testing

import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.remote.model.*
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

object TestData {

    const val COUNTRY_CODE_BRA = "BRA"
    const val COUNTRY_NAME_BRA = "Brazil"
    const val FLAG_URL_BRA = "https://flagcdn.com/br.png"
    const val CAPITAL_BRA = "Brasília"

    fun createCountry(
        cca3: String = COUNTRY_CODE_BRA,
        name: String = COUNTRY_NAME_BRA,
        flagUrl: String = FLAG_URL_BRA
    ) = Country(
        cca3 = cca3,
        commonName = name,
        capital = CAPITAL_BRA,
        flagUrl = flagUrl,
        region = "Americas",
        independent = true
    )

    fun createCountryDetail(
        cca3: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA
    ) = CountryDetail(
        cca3 = cca3,
        commonName = commonName,
        officialName = "Federative Republic of $commonName",
        capital = CAPITAL_BRA,
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        subregion = "South America",
        languages = listOf("Portuguese"),
        population = 215_000_000L,
        borders = listOf("ARG", "BOL"),
        currencies = listOf("Real"),
        independent = true
    )

    fun createCountryEntity(
        cca3: String = COUNTRY_CODE_BRA,
        name: String = COUNTRY_NAME_BRA,
        searchQuery: String = Constants.MAIN_LIST_QUERY_ID
    ) = CountryEntity(
        cca3 = cca3,
        commonName = name,
        capital = CAPITAL_BRA,
        flagUrl = FLAG_URL_BRA,
        region = "Americas",
        independent = true,
        searchQuery = searchQuery
    )

    fun createCountryDetailEntity(
        cca3: String = COUNTRY_CODE_BRA,
        commonName: String = COUNTRY_NAME_BRA
    ) = CountryDetailEntity(
        cca3 = cca3,
        commonName = commonName,
        officialName = "Official $commonName",
        capital = CAPITAL_BRA,
        flagUrl = FLAG_URL_BRA,
        region = "Region",
        subregion = "Subregion",
        languages = listOf("Language"),
        population = 1000L,
        borders = listOf("BORDER"),
        currencies = listOf("Currency"),
        independent = true
    )

    fun createCountryRemote(cca3: String = COUNTRY_CODE_BRA) = CountryRemote(
        codes = CodesRemote(alpha3 = cca3),
        names = NameRemote(common = COUNTRY_NAME_BRA, official = "Official $COUNTRY_NAME_BRA"),
        capitals = listOf(CapitalRemote(name = CAPITAL_BRA)),
        flag = FlagRemote(png = FLAG_URL_BRA, svg = FLAG_URL_BRA),
        region = "Region",
        subregion = "Subregion",
        languages = listOf(LanguageRemote(name = "Language")),
        population = 1000L,
        borders = listOf("BORDER"),
        currencies = listOf(CurrencyRemote(name = "Currency")),
        classification = ClassificationRemote(dependency = false)
    )
}
