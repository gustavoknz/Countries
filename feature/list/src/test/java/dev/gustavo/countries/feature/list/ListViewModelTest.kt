package dev.gustavo.countries.feature.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListViewModelTest {

    private val getCountriesUseCase: GetCountriesUseCase = mockk()
    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()
    private lateinit var viewModel: ListViewModel

    private val countries = listOf(
        Country(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas"),
        Country(cca3 = "PRT", commonName = "Portugal", capital = "Lisbon", flagUrl = "", region = "Europe"),
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ListViewModel(getCountriesUseCase, searchCountriesUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── LoadCountries ─────────────────────────────────────────────────────────

    @Test
    fun `given success when LoadCountries then viewState is Loaded with countries`() = runTest {
        coEvery { getCountriesUseCase() } returns Result.success(countries)

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            viewModel.onAction(ListAction.LoadCountries)
            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.countries).hasSize(2)
            assertThat(loaded.searchQuery).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when LoadCountries then viewState is Error`() = runTest {
        coEvery { getCountriesUseCase() } returns Result.failure(RuntimeException("Network error"))

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            viewModel.onAction(ListAction.LoadCountries)
            val error = awaitItem() as ListViewState.Error
            assertThat(error.message).isEqualTo("Network error")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given success when LoadCountries called twice then api is called twice`() = runTest {
        coEvery { getCountriesUseCase() } returns Result.success(countries)

        viewModel.onAction(ListAction.LoadCountries)
        viewModel.onAction(ListAction.LoadCountries)

        coVerify(exactly = 2) { getCountriesUseCase() }
    }

    // ── SearchQueryChanged ────────────────────────────────────────────────────

    @Test
    fun `given success when SearchQueryChanged then viewState is Loaded with query`() = runTest {
        val filtered = listOf(countries[0])
        coEvery { searchCountriesUseCase("bra") } returns Result.success(filtered)

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            viewModel.onAction(ListAction.SearchQueryChanged("bra"))
            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.countries).hasSize(1)
            assertThat(loaded.searchQuery).isEqualTo("bra")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when SearchQueryChanged then viewState is Error`() = runTest {
        coEvery { searchCountriesUseCase(any()) } returns Result.failure(RuntimeException("Search error"))

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            viewModel.onAction(ListAction.SearchQueryChanged("xyz"))
            val error = awaitItem() as ListViewState.Error
            assertThat(error.message).isEqualTo("Search error")
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── CountryClicked ────────────────────────────────────────────────────────

    @Test
    fun `given country code when CountryClicked then emits NavigateToDetail event`() = runTest {
        viewModel.events.test {
            viewModel.onAction(ListAction.CountryClicked("BRA"))
            assertThat(awaitItem()).isEqualTo(ListEvent.NavigateToDetail("BRA"))
        }
    }

    @Test
    fun `given multiple clicks when CountryClicked then emits event for each click`() = runTest {
        viewModel.events.test {
            viewModel.onAction(ListAction.CountryClicked("BRA"))
            viewModel.onAction(ListAction.CountryClicked("PRT"))
            assertThat(awaitItem()).isEqualTo(ListEvent.NavigateToDetail("BRA"))
            assertThat(awaitItem()).isEqualTo(ListEvent.NavigateToDetail("PRT"))
        }
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `given fresh viewModel then initial state is Loading`() = runTest {
        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
