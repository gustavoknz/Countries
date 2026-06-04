package dev.gustavo.countries.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

import javax.inject.Inject

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override fun io() = Dispatchers.IO
    override fun main() = Dispatchers.Main
    override fun default() = Dispatchers.Default
}
