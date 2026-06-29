package dev.gustavo.countries.domain.usecase

import androidx.paging.PagingData
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.model.CountryQuery
import dev.gustavo.countries.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCountriesUseCase @Inject constructor(private val repository: CountryRepository) {
    operator fun invoke(query: String): Flow<PagingData<Country>> =
        repository.getCountries(query = CountryQuery(text = query.takeIf { it.isNotBlank() }))
}
