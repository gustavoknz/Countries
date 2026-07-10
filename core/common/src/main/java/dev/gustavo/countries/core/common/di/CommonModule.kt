package dev.gustavo.countries.core.common.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.core.common.DefaultDispatcherProvider
import dev.gustavo.countries.core.common.DispatcherProvider
import dev.gustavo.countries.core.common.NetworkConnectivityObserver
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver
    ): ConnectivityObserver

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        dispatcherProvider: DefaultDispatcherProvider
    ): DispatcherProvider

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }
}
