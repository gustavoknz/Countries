package dev.gustavo.countries.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.gustavo.countries.core.common.DispatcherProvider
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.entity.toDomain
import dev.gustavo.countries.data.local.entity.toEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDetailDomain
import dev.gustavo.countries.data.remote.model.toDomain
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val api: CountryApiService,
    private val countryDao: CountryDao,
    private val countryDetailDao: CountryDetailDao,
    private val dispatchers: DispatcherProvider
) : CountryRepository {

    override fun getCountries(query: String?, forceRefresh: Boolean): Flow<PagingData<Country>> {
        return Pager(
            config = PagingConfig(
                pageSize = CountryPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CountryPagingSource(api, query) }
        ).flow
    }

    override suspend fun searchCountries(query: String): Result<List<Country>> = withContext(dispatchers.io()) {
        runCatching {
            if (query.isBlank()) {
                countryDao.getAllCountries().map { it.toDomain() }
            } else {
                countryDao.searchCountries(query).map { it.toDomain() }
            }
        }
    }

    override suspend fun getCountryDetail(cca3: String): Result<CountryDetail> =
        withContext(dispatchers.io()) {
            runCatching {
                countryDetailDao.getByCode(cca3)
                    ?.toDomain()
                    ?: run {
                        val response = api.getCountryDetail(cca3)
                        val objects = response.data?.objects
                        val detail = objects?.firstOrNull()?.toDetailDomain()

                        if (detail == null || detail.cca3.isBlank()) {
                            throw IllegalArgumentException("Country '$cca3' not found or invalid")
                        } else {
                            countryDetailDao.insert(detail.toEntity())
                            detail
                        }
                    }
            }
        }
}
