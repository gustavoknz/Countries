package dev.gustavo.countries.feature.list

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class ListViewModelTest {

    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ListViewModel

    private val connectivityStatus = MutableStateFlow(ConnectivityObserver.Status.Available)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { connectivityObserver.status } returns connectivityStatus
        every { searchCountriesUseCase(any()) } returns flowOf(PagingData.empty())
        viewModel = ListViewModel(searchCountriesUseCase, connectivityObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when SearchQueryChanged action then updates searchQuery state`() = runTest {
        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEqualTo("")

            viewModel.onAction(ListAction.SearchQueryChanged("bra"))
            assertThat(awaitItem()).isEqualTo("bra")
        }
    }

    @Test
    fun `given country code when CountryClicked then emits NavigateToDetail event`() = runTest {
        val cca3 = "BRA"
        val flagUrl = "https://flag.url"
        viewModel.events.test {
            viewModel.onAction(ListAction.CountryClicked(cca3, flagUrl))
            assertThat(awaitItem()).isEqualTo(ListEvent.NavigateToDetail(cca3, flagUrl))
        }
    }

    @Test
    fun `when connectivity status changes then updates isOffline state`() = runTest {
        viewModel.isOffline.test {
            assertThat(awaitItem()).isFalse() // Available

            connectivityStatus.value = ConnectivityObserver.Status.Unavailable
            runCurrent()
            assertThat(awaitItem()).isTrue()

            connectivityStatus.value = ConnectivityObserver.Status.Available
            runCurrent()
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun `given non-blank search query when enough time passes then calls use case with query`() = runTest {
        val query = "brazil"

        backgroundScope.launch { viewModel.countries.collect() }

        viewModel.onAction(ListAction.SearchQueryChanged(query))

        // Before debounce (500ms)
        advanceTimeBy(400.milliseconds)
        runCurrent()
        verify(exactly = 0) { searchCountriesUseCase(query) }

        // After debounce
        advanceTimeBy(101.milliseconds)
        runCurrent()
        verify(exactly = 1) { searchCountriesUseCase(query) }
    }

    @Test
    fun `given blank search query when onAction called then calls use case immediately`() = runTest {
        val query = ""

        backgroundScope.launch { viewModel.countries.collect() }

        viewModel.onAction(ListAction.SearchQueryChanged(query))

        // No time advance needed for blank query due to 0ms debounce optimization
        runCurrent()
        verify(exactly = 1) { searchCountriesUseCase(query) }
    }

    @Test
    fun `given multiple search queries when typing quickly then only last query is executed`() = runTest {
        backgroundScope.launch { viewModel.countries.collect() }

        viewModel.onAction(ListAction.SearchQueryChanged("b"))
        advanceTimeBy(200.milliseconds)
        viewModel.onAction(ListAction.SearchQueryChanged("br"))
        advanceTimeBy(200.milliseconds)
        viewModel.onAction(ListAction.SearchQueryChanged("bra"))

        advanceTimeBy(501.milliseconds)
        runCurrent()

        verify(exactly = 1) { searchCountriesUseCase("bra") }
        verify(exactly = 0) { searchCountriesUseCase("b") }
        verify(exactly = 0) { searchCountriesUseCase("br") }
    }
}
