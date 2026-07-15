package dev.gustavo.countries.core.common

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class DispatcherProviderTest {

    private val dispatcherProvider = DefaultDispatcherProvider()

    @Test
    fun `main should return Dispatchers Main`() {
        assertThat(dispatcherProvider.main()).isEqualTo(Dispatchers.Main)
    }

    @Test
    fun `io should return Dispatchers IO`() {
        assertThat(dispatcherProvider.io()).isEqualTo(Dispatchers.IO)
    }

    @Test
    fun `default should return Dispatchers Default`() {
        assertThat(dispatcherProvider.default()).isEqualTo(Dispatchers.Default)
    }
}
