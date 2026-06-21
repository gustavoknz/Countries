package dev.gustavo.countries.core.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class NetworkConnectivityObserverTest {

    private val context: Context = mockk()
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val network: Network = mockk()
    private val capabilities: NetworkCapabilities = mockk()
    private val networkRequest: NetworkRequest = mockk()

    private lateinit var observer: NetworkConnectivityObserver

    @Before
    fun setUp() {
        mockkConstructor(NetworkRequest.Builder::class)
        every { anyConstructed<NetworkRequest.Builder>().addCapability(any()) } returns mockk(relaxed = true)
        every { anyConstructed<NetworkRequest.Builder>().build() } returns networkRequest

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        observer = NetworkConnectivityObserver(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given available network when observing then emits Available status`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { connectivityManager.registerNetworkCallback(any<NetworkRequest>(), capture(callbackSlot)) } returns Unit

        // Setup initial state: available
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        observer.status.test {
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Available)

            // Simulate network lost
            callbackSlot.captured.onLost(network)
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Lost)

            // Simulate network available again
            callbackSlot.captured.onAvailable(network)
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Available)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given unavailable network initially when observing then emits Unavailable status`() = runTest {
        every { connectivityManager.activeNetwork } returns null

        observer.status.test {
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Unavailable)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when flow is cancelled then unregisters callback`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { connectivityManager.registerNetworkCallback(any<NetworkRequest>(), capture(callbackSlot)) } returns Unit

        observer.status.test {
            awaitItem() // Initial emission
            cancelAndIgnoreRemainingEvents()
        }

        verify { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) }
    }

    @Test
    fun `test all status transitions`() = runTest {
        val callbackSlot = slot<ConnectivityManager.NetworkCallback>()
        every { connectivityManager.registerNetworkCallback(any<NetworkRequest>(), capture(callbackSlot)) } returns Unit
        every { connectivityManager.activeNetwork } returns null

        observer.status.test {
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Unavailable)

            callbackSlot.captured.onAvailable(network)
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Available)

            callbackSlot.captured.onLosing(network, 100)
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Losing)

            callbackSlot.captured.onLost(network)
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Lost)

            callbackSlot.captured.onUnavailable()
            assertThat(awaitItem()).isEqualTo(ConnectivityObserver.Status.Unavailable)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
