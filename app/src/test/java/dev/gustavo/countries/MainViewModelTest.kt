package dev.gustavo.countries

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.common.ConnectivityObserver
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val connectivityObserver: ConnectivityObserver = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    private val connectivityStatus = MutableStateFlow(ConnectivityObserver.Status.Available)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { connectivityObserver.status } returns connectivityStatus
        viewModel = MainViewModel(connectivityObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given available connection when initialized then showConnectivitySnackbar is false`() = runTest {
        viewModel.showConnectivitySnackbar.test {
            assertThat(awaitItem()).isFalse()
            runCurrent()
            expectNoEvents()
        }
    }

    @Test
    fun `given unavailable connection when initialized then showConnectivitySnackbar is true`() = runTest {
        connectivityStatus.value = ConnectivityObserver.Status.Unavailable

        viewModel.showConnectivitySnackbar.test {
            // StateIn initial value is false
            assertThat(awaitItem()).isFalse()

            // After collection starts and flow processes, it should emit true
            runCurrent()
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `given offline then online when connection becomes available then snackbar is reset`() = runTest {
        viewModel.showConnectivitySnackbar.test {
            assertThat(awaitItem()).isFalse() // initial

            connectivityStatus.value = ConnectivityObserver.Status.Unavailable
            runCurrent()
            assertThat(awaitItem()).isTrue()

            viewModel.dismissSnackbar()
            runCurrent()
            assertThat(awaitItem()).isFalse()

            connectivityStatus.value = ConnectivityObserver.Status.Available
            runCurrent()
            // isOffline (false) && !dismissed -> false. 
            // The important part is that _isDismissed becomes false internally.

            connectivityStatus.value = ConnectivityObserver.Status.Unavailable
            runCurrent()
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `given snackbar shown when dismissed then showConnectivitySnackbar becomes false`() = runTest {
        connectivityStatus.value = ConnectivityObserver.Status.Unavailable

        viewModel.showConnectivitySnackbar.test {
            assertThat(awaitItem()).isFalse() // initial
            runCurrent()
            assertThat(awaitItem()).isTrue()

            viewModel.dismissSnackbar()
            runCurrent()
            assertThat(awaitItem()).isFalse()
        }
    }
}
