package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository

class SearchCountriesUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(query: String): Result<List<Country>> =
        repository.getCountries().map { countries ->
            if (query.isBlank()) countries
            else countries.filter { it.commonName.contains(query, ignoreCase = true) }
        }
}
