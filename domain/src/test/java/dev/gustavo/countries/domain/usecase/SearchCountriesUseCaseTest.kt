package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.CountryQuery
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

@Suppress("UNUSED_EXPRESSION", "CheckResult")
class SearchCountriesUseCaseTest {

    private val repository: CountryRepository = mockk()
    private lateinit var useCase: SearchCountriesUseCase

    @Before
    fun setUp() {
        useCase = SearchCountriesUseCase(repository)
    }

    @Test
    fun `given query and region when invoke then calls repository with query and region`() {
        val query = "brazil"
        val region = "Americas"
        val expectedQuery = CountryQuery(text = query, region = region)
        every { repository.getCountries(expectedQuery) } returns flowOf()

        val result = useCase(query, region)

        assertThat(result).isNotNull()
        verify(exactly = 1) {
            repository.getCountries(expectedQuery)
            Unit
        }
    }

    @Test
    fun `given non-blank query when invoke then calls repository with query`() {
        val query = "brazil"
        val expectedQuery = CountryQuery(text = query)
        every { repository.getCountries(expectedQuery) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { 
            repository.getCountries(expectedQuery)
            Unit
        }
    }

    @Test
    fun `given blank query when invoke then calls repository with blank query`() {
        val query = "  "
        val expectedQuery = CountryQuery(text = query)
        every { repository.getCountries(expectedQuery) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { 
            repository.getCountries(expectedQuery)
            Unit
        }
    }

    @Test
    fun `given empty query when invoke then calls repository with empty query`() {
        val query = ""
        val expectedQuery = CountryQuery(text = query)
        every { repository.getCountries(expectedQuery) } returns flowOf()

        val result = useCase(query)

        assertThat(result).isNotNull()
        verify(exactly = 1) { 
            repository.getCountries(expectedQuery)
            Unit
        }
    }
}
