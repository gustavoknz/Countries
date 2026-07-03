package dev.gustavo.countries.core.common

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DataErrorTest {

    @Test
    fun `given UnknownHostException when toDataError then returns NoConnection`() {
        val throwable = UnknownHostException()
        assertThat(throwable.toDataError()).isEqualTo(DataError.NoConnection)
    }

    @Test
    fun `given ConnectException when toDataError then returns NoConnection`() {
        val throwable = ConnectException()
        assertThat(throwable.toDataError()).isEqualTo(DataError.NoConnection)
    }

    @Test
    fun `given SocketTimeoutException when toDataError then returns Timeout`() {
        val throwable = SocketTimeoutException()
        assertThat(throwable.toDataError()).isEqualTo(DataError.Timeout)
    }

    @Test
    fun `given 403 HttpException when toDataError then returns Forbidden`() {
        val response = Response.error<Any>(403, "".toResponseBody())
        val throwable = HttpException(response)
        assertThat(throwable.toDataError()).isEqualTo(DataError.Forbidden)
    }

    @Test
    fun `given 500 HttpException when toDataError then returns ServerError`() {
        val response = Response.error<Any>(500, "".toResponseBody())
        val throwable = HttpException(response)
        assertThat(throwable.toDataError()).isEqualTo(DataError.ServerError)
    }

    @Test
    fun `given SerializationException when toDataError then returns Serialization`() {
        val throwable = SerializationException("error")
        assertThat(throwable.toDataError()).isEqualTo(DataError.Serialization)
    }

    @Test
    fun `given JsonParseException when toDataError then returns Serialization`() {
        val throwable = JsonParseException("error")
        assertThat(throwable.toDataError()).isEqualTo(DataError.Serialization)
    }

    @Test
    fun `given CountryNotFoundException when toDataError then returns NotFound`() {
        val cca3 = "BRA"
        val throwable = CountryNotFoundException(cca3)
        assertThat(throwable.toDataError()).isEqualTo(DataError.NotFound)
        assertThat(throwable.cca3).isEqualTo(cca3)
    }

    @Test
    fun `given generic Exception when toDataError then returns Unknown`() {
        val throwable = RuntimeException()
        assertThat(throwable.toDataError()).isEqualTo(DataError.Unknown)
    }

    @Test(expected = CancellationException::class)
    fun `given CancellationException when toDataError then throws it`() {
        val throwable = CancellationException()
        throwable.toDataError()
    }

    @Test
    fun `given success block when suspendRunCatching then returns success result`() = runTest {
        val result = suspendRunCatching { "success" }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("success")
    }

    @Test
    fun `given failure block when suspendRunCatching then returns failure result`() = runTest {
        val exception = RuntimeException("error")
        val result = suspendRunCatching { throw exception }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test(expected = CancellationException::class)
    fun `given cancellation when suspendRunCatching then throws it`() = runTest {
        suspendRunCatching { throw CancellationException() }
    }
}
