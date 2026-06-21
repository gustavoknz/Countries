package dev.gustavo.countries.feature.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        every { searchCountriesUseCase(any()) } returns flowOf()
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
}
