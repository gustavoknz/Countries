package dev.gustavo.countries.core.common

import android.R.id.message

sealed interface NetworkError {
    data object NoConnection : NetworkError
    data class HttpError(val code: Int, val message: String) : NetworkError
    data class Unknown(val throwable: Throwable) : NetworkError
}

fun Throwable.toNetworkError(): NetworkError = when (this) {
    is java.net.UnknownHostException,
    is java.net.ConnectException -> NetworkError.NoConnection
    is retrofit2.HttpException -> NetworkError.HttpError(code(), message())
    else -> NetworkError.Unknown(this)
}
