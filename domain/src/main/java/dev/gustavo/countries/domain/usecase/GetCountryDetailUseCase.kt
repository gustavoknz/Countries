package dev.gustavo.countries.domain.usecase

import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountryDetailUseCase
    @Inject
    constructor(
        private val repository: CountryRepository,
    ) {
        suspend operator fun invoke(countryCode: String): Result<CountryDetail> =
            repository.getCountryDetail(countryCode)
    }
