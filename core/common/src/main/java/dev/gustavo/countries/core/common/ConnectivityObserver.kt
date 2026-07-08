package dev.gustavo.countries.core.common

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val status: Flow<Status>

    enum class Status {
        Available,
        Losing,
        Lost,
        Unavailable
    }
}
