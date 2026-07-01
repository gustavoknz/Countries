package dev.gustavo.countries.domain.repository

import androidx.paging.PagingData
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.model.CountryQuery
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountries(query: CountryQuery = CountryQuery()): Flow<PagingData<Country>>
    suspend fun getCountryDetail(cca3: String): Result<CountryDetail>
}
