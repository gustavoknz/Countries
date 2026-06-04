package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository

class GetCountriesUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(): Result<List<Country>> = repository.getCountries()
}
