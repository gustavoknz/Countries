package dev.gustavo.countries.core.common

import kotlinx.coroutines.CancellationException

sealed interface DataError {
    data object NoConnection : DataError
    data object Timeout : DataError
    data object ServerError : DataError
    data object Serialization : DataError
    data object Unknown : DataError
}

fun Throwable.toDataError(): DataError {
    if (this is CancellationException) throw this
    return when (this) {
        is java.net.UnknownHostException,
        is java.net.ConnectException -> DataError.NoConnection

        is java.net.SocketTimeoutException -> DataError.Timeout
        is retrofit2.HttpException -> DataError.ServerError
        is kotlinx.serialization.SerializationException -> DataError.Serialization
        else -> {
            if (this.javaClass.name.contains("JsonParseException")) {
                DataError.Serialization
            } else {
                DataError.Unknown
            }
        }
    }
}

suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        Result.failure(e)
    }
}
