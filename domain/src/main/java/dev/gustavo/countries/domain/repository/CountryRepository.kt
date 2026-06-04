package dev.gustavo.countries.domain.repository

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryDetail

interface CountryRepository {
    suspend fun getCountries(): Result<List<Country>>
    suspend fun getCountryDetail(cca3: String): Result<CountryDetail>
}
