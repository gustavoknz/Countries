package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.repository.CountryRepository

class GetCountryDetailUseCase(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(cca3: String): Result<CountryDetail> =
        repository.getCountryDetail(cca3)
}
