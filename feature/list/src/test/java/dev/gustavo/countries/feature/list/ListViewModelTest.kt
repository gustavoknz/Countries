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
        every { searchCountriesUseCase(any(), any()) } returns flowOf()
        viewModel = ListViewModel(searchCountriesUseCase, connectivityObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given country code when CountryClicked then emits NavigateToDetail event`() = runTest {
        viewModel.events.test {
            viewModel.onAction(ListAction.CountryClicked("BRA"))
            assertThat(awaitItem()).isEqualTo(ListEvent.NavigateToDetail("BRA"))
        }
    }
}
