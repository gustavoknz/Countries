package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val cca3: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
)

@Entity(tableName = "country_details")
data class CountryDetailEntity(
    @PrimaryKey val cca3: String,
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
