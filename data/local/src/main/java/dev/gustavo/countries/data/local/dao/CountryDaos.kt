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

    @Query("""
        SELECT * FROM countries 
        WHERE searchQuery = :query 
        AND (:region IS NULL OR region = :region)
        ORDER BY commonName ASC
    """)
    fun getCountriesPaging(query: String, region: String?): PagingSource<Int, CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Transaction
    suspend fun clearSearchCache(queryId: String) {
        deleteSearchCountries(queryId)
        if (queryId != Constants.MAIN_LIST_QUERY_ID) {
            deleteOtherSearches(queryId)
        } else {
            deleteAllSearches()
        }
    }

    @Query("DELETE FROM countries WHERE searchQuery = '${Constants.MAIN_LIST_QUERY_ID}'")
    suspend fun deletePagedCountries()

    @Query("DELETE FROM countries WHERE searchQuery = :query")
    suspend fun deleteSearchCountries(query: String)

    @Query("DELETE FROM countries WHERE searchQuery != :query AND searchQuery != '${Constants.MAIN_LIST_QUERY_ID}'")
    suspend fun deleteOtherSearches(query: String)

    @Query("DELETE FROM countries WHERE searchQuery != '${Constants.MAIN_LIST_QUERY_ID}'")
    suspend fun deleteAllSearches()
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

    @Transaction
    suspend fun clearSearchCache(id: String) {
        deleteRemoteKey(id)
        if (id != RemoteKeyEntity.COUNTRIES_LIST_ID) {
            deleteOtherSearchKeys(id)
        } else {
            deleteAllSearchKeys()
        }
    }

    @Query("DELETE FROM remote_keys WHERE id = :id")
    suspend fun deleteRemoteKey(id: String)

    @Query("DELETE FROM remote_keys WHERE id != :id AND id != '${RemoteKeyEntity.COUNTRIES_LIST_ID}'")
    suspend fun deleteOtherSearchKeys(id: String)

    @Query("DELETE FROM remote_keys WHERE id != '${RemoteKeyEntity.COUNTRIES_LIST_ID}'")
    suspend fun deleteAllSearchKeys()
}
