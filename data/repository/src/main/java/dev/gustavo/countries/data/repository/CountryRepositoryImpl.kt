package dev.gustavo.countries.data.repository

import dev.gustavo.countries.core.common.DispatcherProvider
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.entity.toDomain
import dev.gustavo.countries.data.local.entity.toEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDomain
import dev.gustavo.countries.data.remote.model.toDetailDomain
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.repository.CountryRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val api: CountryApiService,
    private val countryDao: CountryDao,
    private val countryDetailDao: CountryDetailDao,
    private val dispatchers: DispatcherProvider
) : CountryRepository {

    override suspend fun getCountries(forceRefresh: Boolean): Result<List<Country>> = withContext(dispatchers.io()) {
        runCatching {
            if (!forceRefresh) {
                val cached = countryDao.getAllCountries()
                if (cached.isNotEmpty()) return@runCatching cached.map { it.toDomain() }
            }

            val remote = api.getAllCountries().map { it.toDomain() }
            if (forceRefresh) {
                countryDao.deleteAll()
            }
            countryDao.insertAll(remote.map { it.toEntity() })
            countryDao.getAllCountries().map { it.toDomain() }
        }
    }

    override suspend fun getCountryDetail(cca3: String): Result<CountryDetail> =
        withContext(dispatchers.io()) {
            runCatching {
                countryDetailDao.getByCode(cca3)
                    ?.toDomain()
                    ?: run {
                        val remote = api.getCountryDetail(cca3)
                        if (remote.isEmpty()) {
                            throw IllegalArgumentException("Country '$cca3' not found")
                        } else {
                            val detail = remote[0].toDetailDomain()
                            countryDetailDao.insert(detail.toEntity())
                            detail
                        }
                    }
            }
        }
}
