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
import dev.gustavo.countries.data.local.entity.CountrySearchResultEntity
import dev.gustavo.countries.data.local.entity.RemoteKeyEntity

@Dao
interface CountryDao {

    @Query(
        """SELECT countries.* FROM countries 
        INNER JOIN country_search_results ON countries.cca3 = country_search_results.cca3
        WHERE country_search_results.queryId = :queryId 
        AND (:region IS NULL OR countries.region = :region)
        ORDER BY country_search_results.createdAt ASC, countries.commonName ASC"""
    )
    fun getCountriesPaging(queryId: String, region: String?): PagingSource<Int, CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(searchResults: List<CountrySearchResultEntity>)

    @Transaction
    suspend fun clearSearchCache(queryId: String) {
        deleteSearchResultsForQuery(queryId)
        if (queryId != Constants.MAIN_LIST_QUERY_ID) {
            deleteOtherSearchResults(queryId)
        } else {
            deleteAllSearchResults()
        }
        // Prune orphaned countries that are not in any search result and not detailed
        pruneOrphanedCountries()
    }

    @Query("DELETE FROM country_search_results WHERE queryId = :queryId")
    suspend fun deleteSearchResultsForQuery(queryId: String)

    @Query(
        """DELETE FROM country_search_results 
        WHERE queryId != :queryId AND queryId != '${Constants.MAIN_LIST_QUERY_ID}'"""
    )
    suspend fun deleteOtherSearchResults(queryId: String)

    @Query("DELETE FROM country_search_results WHERE queryId != '${Constants.MAIN_LIST_QUERY_ID}'")
    suspend fun deleteAllSearchResults()

    @Query(
        """DELETE FROM countries 
        WHERE cca3 NOT IN (SELECT DISTINCT cca3 FROM country_search_results)
        AND cca3 NOT IN (SELECT DISTINCT cca3 FROM country_details)"""
    )
    suspend fun pruneOrphanedCountries()
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
