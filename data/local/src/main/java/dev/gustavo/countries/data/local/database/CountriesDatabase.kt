package dev.gustavo.countries.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity

@Database(
    entities = [CountryEntity::class, CountryDetailEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun countryDetailDao(): CountryDetailDao
}
