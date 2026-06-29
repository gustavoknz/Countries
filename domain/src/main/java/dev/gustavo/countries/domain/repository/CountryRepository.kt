package dev.gustavo.countries.domain.repository

import androidx.paging.PagingData
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail
import kotlinx.coroutines.flow.Flow

import dev.gustavo.countries.domain.model.CountryQuery

interface CountryRepository {
    fun getCountries(query: CountryQuery = CountryQuery()): Flow<PagingData<Country>>
    suspend fun getCountryDetail(cca3: String): Result<CountryDetail>
}
