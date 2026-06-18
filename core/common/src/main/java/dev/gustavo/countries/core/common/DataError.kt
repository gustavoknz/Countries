package dev.gustavo.countries.core.common

import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface DataError {
    data object NoConnection : DataError
    data object Timeout : DataError
    data object ServerError : DataError
    data object Forbidden : DataError
    data object Serialization : DataError
    data object Unknown : DataError
}

fun Throwable.toDataError(): DataError {
    if (this is CancellationException) throw this
    return when (this) {
        is UnknownHostException,
        is ConnectException -> DataError.NoConnection

        is SocketTimeoutException -> DataError.Timeout
        is HttpException -> {
            if (this.code() == 403) DataError.Forbidden
            else DataError.ServerError
        }
        is SerializationException,
        is JsonParseException -> DataError.Serialization

        else -> DataError.Unknown
    }
}

suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(t)
    }
}
