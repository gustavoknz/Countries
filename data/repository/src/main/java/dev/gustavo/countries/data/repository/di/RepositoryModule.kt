package dev.gustavo.countries.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gustavo.countries.core.common.DefaultDispatcherProvider
import dev.gustavo.countries.core.common.DispatcherProvider
import dev.gustavo.countries.data.repository.CountryRepositoryImpl
import dev.gustavo.countries.domain.repository.CountryRepository
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCountryRepository(impl: CountryRepositoryImpl): CountryRepository

    @Binds
    abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCountriesUseCase(repository: CountryRepository): GetCountriesUseCase =
        GetCountriesUseCase(repository)

    @Provides
    fun provideSearchCountriesUseCase(repository: CountryRepository): SearchCountriesUseCase =
        SearchCountriesUseCase(repository)

    @Provides
    fun provideGetCountryDetailUseCase(repository: CountryRepository): GetCountryDetailUseCase =
        GetCountryDetailUseCase(repository)
}
