package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `given non-blank query when invoke then calls repository with query`() {
        val query = "brazil"
        every { repository.getCountries(query) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { repository.getCountries(query) }
    }

    @Test
    fun `given blank query when invoke then calls repository with null`() {
        val query = "  "
        every { repository.getCountries(null) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { repository.getCountries(null) }
    }

    @Test
    fun `given empty query when invoke then calls repository with null`() {
        val query = ""
        every { repository.getCountries(null) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { repository.getCountries(null) }
    }
}
