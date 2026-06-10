package dev.gustavo.countries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY commonName ASC")
    suspend fun getAllCountries(): List<CountryEntity>

    @Query("SELECT * FROM countries WHERE commonName LIKE '%' || :query || '%' ORDER BY commonName ASC")
    suspend fun searchCountries(query: String): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Query("DELETE FROM countries")
    suspend fun deleteAll()
}

@Dao
interface CountryDetailDao {

    @Query("SELECT * FROM country_details WHERE cca3 = :cca3")
    suspend fun getByCode(cca3: String): CountryDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: CountryDetailEntity)
}
