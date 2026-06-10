package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: SearchCountriesUseCase

    private val allCountries = listOf(
        Country(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas", independent = true),
        Country(cca3 = "PRT", commonName = "Portugal", capital = "Lisbon", flagUrl = "", region = "Europe", independent = true),
        Country(cca3 = "FRA", commonName = "France", capital = "Paris", flagUrl = "", region = "Europe", independent = true)
    )

    @Before
    fun setUp() {
        useCase = SearchCountriesUseCase(repository)
    }

    @Test
    fun `given blank query when invoke then returns all countries`() = runTest {
        coEvery { repository.searchCountries("") } returns Result.success(allCountries)
        
        val result = useCase("")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(3)
    }

    @Test
    fun `given matching query when invoke then returns filtered list from repository`() = runTest {
        val filteredList = listOf(allCountries[0])
        coEvery { repository.searchCountries("bra") } returns Result.success(filteredList)

        val result = useCase("bra")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(allCountries[0])
    }

    @Test
    fun `given repository failure when invoke then returns failure`() = runTest {
        coEvery { repository.searchCountries("bra") } returns Result.failure(RuntimeException("error"))

        val result = useCase("bra")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `given forceRefresh true when invoke then fetches all and filters in memory`() = runTest {
        coEvery { repository.getCountries(true) } returns Result.success(allCountries)

        val result = useCase("bra", forceRefresh = true)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(allCountries[0])
    }
}
