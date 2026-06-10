package dev.gustavo.countries.domain.model

data class CountryDetail(
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
    val currencies: List<String>
)
