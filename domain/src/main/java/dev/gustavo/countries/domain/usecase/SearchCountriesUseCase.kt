package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import javax.inject.Inject

class SearchCountriesUseCase @Inject constructor(private val repository: CountryRepository) {
    suspend operator fun invoke(query: String, forceRefresh: Boolean = false): Result<List<Country>> =
        repository.getCountries(forceRefresh).map { countries ->
            if (query.isBlank()) countries
            else countries.filter { it.commonName.contains(query, ignoreCase = true) }
        }
}
