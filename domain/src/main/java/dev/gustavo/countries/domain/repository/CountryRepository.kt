package dev.gustavo.countries.domain.repository

import androidx.paging.PagingData
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(query: String? = null, forceRefresh: Boolean = false): Flow<PagingData<Country>>
    suspend fun searchCountries(query: String): Result<List<Country>>
    suspend fun getCountryDetail(cca3: String): Result<CountryDetail>
}
