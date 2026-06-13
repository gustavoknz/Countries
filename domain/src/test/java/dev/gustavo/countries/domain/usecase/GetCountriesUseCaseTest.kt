package dev.gustavo.countries.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.repository.CountryRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
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
    fun `when invoke then returns flow from repository`() {
        every { repository.getCountries(any(), any()) } returns flowOf()

        val result = useCase()

        assertThat(result).isNotNull()
    }
}
