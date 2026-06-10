package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: GetCountriesUseCase

    @Before
    fun setUp() {
        useCase = GetCountriesUseCase(repository)
    }

    @Test
    fun `given success when invoke then returns country list`() = runTest {
        val countries = listOf(
            Country(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas", independent = true)
        )
        coEvery { repository.getCountries() } returns Result.success(countries)

        val result = useCase()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(countries)
        coVerify(exactly = 1) { repository.getCountries() }
    }

    @Test
    fun `given failure when invoke then returns failure`() = runTest {
        val error = RuntimeException("Network error")
        coEvery { repository.getCountries() } returns Result.failure(error)

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify(exactly = 1) { repository.getCountries() }
    }
}
