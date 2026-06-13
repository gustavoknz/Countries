package dev.gustavo.countries.domain.usecase

import androidx.paging.PagingData
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val repository: CountryRepository) {
    operator fun invoke(query: String? = null, forceRefresh: Boolean = false): Flow<PagingData<Country>> =
        repository.getCountries(query, forceRefresh)
}
