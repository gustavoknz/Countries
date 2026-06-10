package dev.gustavo.countries.domain.model

data class Country(
    val cca3: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val independent: Boolean
)
