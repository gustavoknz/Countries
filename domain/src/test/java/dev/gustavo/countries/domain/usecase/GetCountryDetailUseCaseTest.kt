package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCountryDetailUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: GetCountryDetailUseCase

    private val detail = CountryDetail(
        cca3 = "PRT",
        commonName = "Portugal",
        officialName = "Portuguese Republic",
        capital = "Lisbon",
        flagUrl = "https://flagcdn.com/pt.png",
        region = "Europe",
        subregion = "Southern Europe",
        languages = listOf("Portuguese"),
        population = 10_300_000L,
        borders = listOf("ESP"),
        currencies = listOf("Euro"),
    )

    @BeforeEach
    fun setUp() {
        useCase = GetCountryDetailUseCase(repository)
    }

    @Test
    fun `given success when invoke then returns country detail`() = runTest {
        coEvery { repository.getCountryDetail("PRT") } returns Result.success(detail)

        val result = useCase("PRT")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(detail)
        coVerify(exactly = 1) { repository.getCountryDetail("PRT") }
    }

    @Test
    fun `given failure when invoke then returns failure`() = runTest {
        coEvery { repository.getCountryDetail("PRT") } returns Result.failure(RuntimeException("Not found"))

        val result = useCase("PRT")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Not found")
        coVerify(exactly = 1) { repository.getCountryDetail("PRT") }
    }

    @Test
    fun `given correct cca3 when invoke then passes it through to repository`() = runTest {
        coEvery { repository.getCountryDetail("PRT") } returns Result.success(detail)

        useCase("PRT")

        coVerify(exactly = 1) { repository.getCountryDetail("PRT") }
    }
}
