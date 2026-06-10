package dev.gustavo.countries.feature.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.Country
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import dev.gustavo.countries.feature.list.model.toUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ListViewModelTest {

    private val getCountriesUseCase: GetCountriesUseCase = mockk()
    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ListViewModel

    private val countries = listOf(
        Country(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas"),
        Country(cca3 = "PRT", commonName = "Portugal", capital = "Lisbon", flagUrl = "", region = "Europe")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListViewModel(getCountriesUseCase, searchCountriesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── LoadCountries ─────────────────────────────────────────────────────────

    @Test
    fun `given success when LoadCountries then viewState is Loaded with countries`() = runTest {
        coEvery { getCountriesUseCase(any()) } returns Result.success(countries)

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            viewModel.onAction(ListAction.LoadCountries)
            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.countries).isEqualTo(countries.map { it.toUiModel() })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when LoadCountries then viewState is Error`() = runTest {
        coEvery { getCountriesUseCase(any()) } returns Result.failure(RuntimeException("Network error"))

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
        coEvery { getCountriesUseCase(any()) } returns Result.success(countries)

        viewModel.onAction(ListAction.LoadCountries)
        runCurrent()
        viewModel.onAction(ListAction.LoadCountries)
        runCurrent()

        coVerify(exactly = 2) { getCountriesUseCase(forceRefresh = false) }
    }

    // ── SearchQueryChanged ────────────────────────────────────────────────────

    @Test
    fun `given query when SearchQueryChanged then searchQuery state is updated immediately`() = runTest {
        coEvery { searchCountriesUseCase(any(), any()) } returns Result.success(emptyList())

        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEmpty()
            viewModel.onAction(ListAction.SearchQueryChanged("bra"))
            assertThat(awaitItem()).isEqualTo("bra")
        }
    }

    @Test
    fun `given success when SearchQueryChanged then viewState is Loaded after debounce`() = runTest {
        val filtered = listOf(countries[0])
        coEvery { searchCountriesUseCase("bra", any()) } returns Result.success(filtered)

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)

            viewModel.onAction(ListAction.SearchQueryChanged("bra"))

            runCurrent()
            expectNoEvents()

            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.countries).isEqualTo(filtered.map { it.toUiModel() })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when SearchQueryChanged then viewState is Error after debounce`() = runTest {
        coEvery { searchCountriesUseCase(any(), any()) } returns Result.failure(RuntimeException("Search error"))

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)

            viewModel.onAction(ListAction.SearchQueryChanged("xyz"))

            val error = awaitItem() as ListViewState.Error
            assertThat(error.message).isEqualTo("Search error")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given multiple rapid changes when SearchQueryChanged then api is called only once`() = runTest {
        coEvery { searchCountriesUseCase(any(), any()) } returns Result.success(emptyList())

        viewModel.onAction(ListAction.SearchQueryChanged("b"))
        viewModel.onAction(ListAction.SearchQueryChanged("br"))
        viewModel.onAction(ListAction.SearchQueryChanged("bra"))

        advanceUntilIdle()

        coVerify(exactly = 0) { searchCountriesUseCase("b", any()) }
        coVerify(exactly = 0) { searchCountriesUseCase("br", any()) }
        coVerify(exactly = 1) { searchCountriesUseCase("bra", any()) }
    }

    @Test
    fun `given SearchTriggered when onAction then api is called immediately`() = runTest {
        coEvery { searchCountriesUseCase(any(), any()) } returns Result.success(emptyList())

        viewModel.onAction(ListAction.SearchQueryChanged("bra"))
        viewModel.onAction(ListAction.SearchTriggered)

        runCurrent()

        coVerify(exactly = 1) { searchCountriesUseCase("bra", forceRefresh = false) }
    }

    @Test
    fun `given success when Refresh then viewState transitions to isRefreshing then Loaded`() = runTest {
        coEvery { getCountriesUseCase(any()) } returns Result.success(countries)

        viewModel.viewState.test {
            // Set initial state to Loaded
            viewModel.onAction(ListAction.LoadCountries)
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loaded::class.java)

            viewModel.onAction(ListAction.Refresh)
            val refreshing = awaitItem() as ListViewState.Loaded
            assertThat(refreshing.isRefreshing).isTrue()

            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.isRefreshing).isFalse()
            assertThat(loaded.countries).hasSize(2)
            
            coVerify(exactly = 1) { getCountriesUseCase(forceRefresh = true) }
        }
    }

    @Test
    fun `given error state when Refresh then viewState transitions to isRefreshing then Loaded`() = runTest {
        coEvery { getCountriesUseCase(forceRefresh = false) } returns Result.failure(RuntimeException("Error"))
        coEvery { getCountriesUseCase(forceRefresh = true) } returns Result.success(countries)

        viewModel.viewState.test {
            // Set initial state to Error
            viewModel.onAction(ListAction.LoadCountries)
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(ListViewState.Error::class.java)

            viewModel.onAction(ListAction.Refresh)
            val refreshing = awaitItem() as ListViewState.Error
            assertThat(refreshing.isRefreshing).isTrue()

            val loaded = awaitItem() as ListViewState.Loaded
            assertThat(loaded.isRefreshing).isFalse()
            assertThat(loaded.countries).hasSize(2)

            coVerify(exactly = 1) { getCountriesUseCase(forceRefresh = true) }
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

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `given fresh viewModel then initial state is Loading`() = runTest {
        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(ListViewState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
