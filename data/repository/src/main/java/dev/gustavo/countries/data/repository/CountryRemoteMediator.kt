package dev.gustavo.countries.data.repository

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.local.entity.RemoteKeyEntity
import dev.gustavo.countries.data.local.entity.toEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDomain

class CountryRemoteMediator(
    private val api: CountryApiService,
    private val database: CountriesDatabase,
    private val query: String? = null
) : RemoteMediator<Int, CountryEntity>() {

    private val countryDao: CountryDao = database.countryDao()
    private val remoteKeyDao: RemoteKeyDao = database.remoteKeyDao()
    private val remoteKeyId = RemoteKeyEntity.getListId(query)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CountryEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKeyById(remoteKeyId)
                    }
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextKey!!
                }
            }

            val response = api.getAllCountries(
                query = query,
                limit = state.config.pageSize,
                offset = offset
            )

            val countries = response.data?.objects
                ?.map { it.toDomain() }
                ?.filter { it.cca3.isNotBlank() }
                ?: emptyList()

            val endOfPaginationReached = response.data?.meta?.more != true

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteRemoteKey(remoteKeyId)
                    if (query == null) {
                        countryDao.deletePagedCountries()
                        countryDao.deleteAllSearches()
                        remoteKeyDao.deleteAllSearchKeys()
                    } else {
                        countryDao.deleteSearchCountries(query)
                        countryDao.deleteOtherSearches(query)
                        remoteKeyDao.deleteOtherSearchKeys(remoteKeyId)
                    }
                }

                val nextKey = if (endOfPaginationReached) null else offset + state.config.pageSize
                remoteKeyDao.insertAll(listOf(RemoteKeyEntity(remoteKeyId, nextKey)))
                countryDao.insertAll(countries.map { it.toEntity(searchQuery = query) })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
