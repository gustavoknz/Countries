package dev.gustavo.countries.domain.model

import kotlinx.collections.immutable.ImmutableList

data class CountryDetail(
    val countryCode: String,
    val commonName: String,
    val officialName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val subregion: String,
    val languages: ImmutableList<String>,
    val population: Long,
    val borders: ImmutableList<String>,
    val currencies: ImmutableList<String>,
    val independent: Boolean,
)
