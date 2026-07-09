package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val cca3: String,
    val commonName: String,
    val capital: String,
    val flagUrl: String,
    val region: String,
    val independent: Boolean
)

@Entity(
    tableName = "country_search_results",
    primaryKeys = ["queryId", "cca3"],
    foreignKeys = [
        ForeignKey(
            entity = CountryEntity::class,
            parentColumns = ["cca3"],
            childColumns = ["cca3"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cca3"])]
)
data class CountrySearchResultEntity(
    val queryId: String,
    val cca3: String,
    val createdAt: Long = System.currentTimeMillis()
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
