package dev.gustavo.countries.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CountriesDatabase {
        return Room.databaseBuilder(context, CountriesDatabase::class.java, "countries_db")
            .build()
    }

    @Provides
    fun provideCountryDao(database: CountriesDatabase): CountryDao = database.countryDao()

    @Provides
    fun provideCountryDetailDao(database: CountriesDatabase): CountryDetailDao = database.countryDetailDao()

    @Provides
    fun provideRemoteKeyDao(database: CountriesDatabase): RemoteKeyDao = database.remoteKeyDao()
}
