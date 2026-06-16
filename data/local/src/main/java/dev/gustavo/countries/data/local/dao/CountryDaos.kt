package dev.gustavo.countries.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.local.entity.RemoteKeyEntity

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries WHERE searchQuery = '${Constants.MAIN_LIST_QUERY_ID}' ORDER BY commonName ASC")
    fun getAllCountriesPaging(): PagingSource<Int, CountryEntity>

    @Query("SELECT * FROM countries WHERE searchQuery = :query ORDER BY commonName ASC")
    fun searchCountriesPaging(query: String): PagingSource<Int, CountryEntity>

    @Query("SELECT * FROM countries WHERE searchQuery = '${Constants.MAIN_LIST_QUERY_ID}' ORDER BY commonName ASC")
    suspend fun getAllCountries(): List<CountryEntity>

    @Query("SELECT * FROM countries WHERE searchQuery = :query ORDER BY commonName ASC")
    suspend fun searchCountries(query: String): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Query("DELETE FROM countries WHERE searchQuery = '${Constants.MAIN_LIST_QUERY_ID}'")
    suspend fun deletePagedCountries()

    @Query("DELETE FROM countries WHERE searchQuery = :query")
    suspend fun deleteSearchCountries(query: String)

    @Query("DELETE FROM countries")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshCountries(countries: List<CountryEntity>) {
        deleteAll()
        insertAll(countries)
    }
}

@Dao
interface CountryDetailDao {

    @Query("SELECT * FROM country_details WHERE cca3 = :cca3")
    suspend fun getByCode(cca3: String): CountryDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: CountryDetailEntity)
}

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeyById(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE id = :id")
    suspend fun deleteRemoteKey(id: String)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}
