package dev.gustavo.countries.data.repository

import android.util.Log
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.core.common.toDataError
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.local.entity.RemoteKeyEntity
import dev.gustavo.countries.data.local.entity.toEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDomain
import dev.gustavo.countries.domain.model.CountryQuery

class CountryRemoteMediator(
    private val api: CountryApiService,
    private val database: CountriesDatabase,
    private val query: CountryQuery
) : RemoteMediator<Int, CountryEntity>() {

    private val countryDao: CountryDao = database.countryDao()
    private val remoteKeyDao: RemoteKeyDao = database.remoteKeyDao()
    private val remoteKeyId = RemoteKeyEntity.getListId(query.text, query.region)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CountryEntity>
    ): MediatorResult {
        return try {
            val queryText = query.text?.takeIf { it.isNotBlank() }
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDao.getRemoteKeyById(remoteKeyId)
                    remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getAllCountries(
                query = queryText,
                region = query.region,
                limit = state.config.pageSize,
                offset = offset
            )

            val countries = response.data?.objects
                ?.asSequence()
                ?.map { it.toDomain() }
                ?.filter { it.cca3.isNotBlank() }
                ?.toList()
                ?: emptyList()

            val endOfPaginationReached = response.data?.meta?.more != true

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    clearCachedData()
                }

                val nextKey = if (endOfPaginationReached) null else offset + state.config.pageSize
                remoteKeyDao.insertAll(listOf(RemoteKeyEntity(remoteKeyId, nextKey)))
                
                val queryId = queryText ?: Constants.MAIN_LIST_QUERY_ID
                countryDao.insertAll(countries.map { it.toEntity(searchQuery = queryId) })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            val dataError = e.toDataError()
            Log.e(
                "CountryRemoteMediator",
                "Error loading countries: loadType=$loadType, query='${query.text}', region='${query.region}', error=$dataError",
                e
            )
            MediatorResult.Error(e)
        }
    }

    private suspend fun clearCachedData() {
        val queryText = query.text?.takeIf { it.isNotBlank() }
        val queryId = queryText ?: Constants.MAIN_LIST_QUERY_ID
        
        remoteKeyDao.deleteRemoteKey(remoteKeyId)
        
        if (queryText == null && query.region == null) {
            countryDao.deletePagedCountries()
            countryDao.deleteAllSearches()
            remoteKeyDao.deleteAllSearchKeys()
        } else {
            // We delete by queryId. If user is filtering by region on main list, 
            // it will clear the main list cache.
            countryDao.deleteSearchCountries(queryId)
            countryDao.deleteOtherSearches(queryId)
            remoteKeyDao.deleteOtherSearchKeys(remoteKeyId)
        }
    }
}
