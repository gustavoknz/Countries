package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class SearchCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: SearchCountriesUseCase

    @Before
    fun setUp() {
        useCase = SearchCountriesUseCase(repository)
    }

    @Test
    fun `when invoke then returns flow from repository`() {
        every { repository.getCountries(any()) } returns flowOf()

        val result = useCase("bra")

        assertThat(result).isNotNull()
    }
}
