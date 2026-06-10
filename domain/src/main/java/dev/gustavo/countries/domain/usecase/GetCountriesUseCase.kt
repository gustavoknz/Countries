package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val repository: CountryRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<List<Country>> = 
        repository.getCountries(forceRefresh)
}
