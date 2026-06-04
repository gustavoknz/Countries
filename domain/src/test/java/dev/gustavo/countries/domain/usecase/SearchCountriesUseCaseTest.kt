package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: SearchCountriesUseCase

    private val allCountries = listOf(
        Country(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas"),
        Country(cca3 = "PRT", commonName = "Portugal", capital = "Lisbon", flagUrl = "", region = "Europe"),
        Country(cca3 = "FRA", commonName = "France", capital = "Paris", flagUrl = "", region = "Europe"),
    )

    @BeforeEach
    fun setUp() {
        useCase = SearchCountriesUseCase(repository)
        coEvery { repository.getCountries() } returns Result.success(allCountries)
    }

    @Test
    fun `given blank query when invoke then returns all countries`() = runTest {
        val result = useCase("")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(3)
    }

    @Test
    fun `given matching query when invoke then returns filtered list`() = runTest {
        val result = useCase("bra")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(allCountries[0])
    }

    @Test
    fun `given case insensitive query when invoke then returns filtered list`() = runTest {
        val result = useCase("PORT")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(allCountries[1])
    }

    @Test
    fun `given non matching query when invoke then returns empty list`() = runTest {
        val result = useCase("xyz")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `given repository failure when invoke then returns failure`() = runTest {
        coEvery { repository.getCountries() } returns Result.failure(RuntimeException("error"))

        val result = useCase("bra")

        assertThat(result.isFailure).isTrue()
    }
}
