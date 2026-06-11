package dev.gustavo.countries.core.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.core.common.NetworkConnectivityObserver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver
    ): ConnectivityObserver
}
