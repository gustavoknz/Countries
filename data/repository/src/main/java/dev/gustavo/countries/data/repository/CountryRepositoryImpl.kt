package dev.gustavo.countries.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.core.common.CountryNotFoundException
import dev.gustavo.countries.core.common.DispatcherProvider
import dev.gustavo.countries.core.common.suspendRunCatching
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.toDomain
import dev.gustavo.countries.data.local.entity.toEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDetailDomain
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.model.CountryQuery
import dev.gustavo.countries.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val api: CountryApiService,
    private val database: CountriesDatabase,
    private val countryDao: CountryDao,
    private val countryDetailDao: CountryDetailDao,
    private val dispatchers: DispatcherProvider
) : CountryRepository {

    override fun getCountries(query: CountryQuery): Flow<PagingData<Country>> {
        val queryText = query.text
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = CountryRemoteMediator(api, database, query),
            pagingSourceFactory = {
                if (queryText.isNullOrBlank()) {
                    countryDao.getAllCountriesPaging()
                } else {
                    countryDao.searchCountriesPaging(queryText)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getCountryDetail(cca3: String): Result<CountryDetail> = withContext(dispatchers.io()) {
        suspendRunCatching {
            countryDetailDao.getByCode(cca3)
                ?.toDomain()
                ?: run {
                    val response = api.getCountryDetail(cca3)
                    val objects = response.data?.objects
                    val detail = objects?.firstOrNull()?.toDetailDomain()

                    if (detail == null || detail.cca3.isBlank()) {
                        throw CountryNotFoundException(cca3)
                    } else {
                        countryDetailDao.insert(detail.toEntity())
                        detail
                    }
                }
        }
    }
}
