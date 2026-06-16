package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.gustavo.countries.core.common.Constants

@Entity(
    tableName = "countries",
    primaryKeys = ["cca3", "searchQuery"]
)
data class CountryEntity(
    val cca3: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val independent: Boolean,
    val searchQuery: String = Constants.MAIN_LIST_QUERY_ID
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
    val independent: Boolean
)
