package dev.gustavo.countries.data.local.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.database.StringListConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideStringListConverter(gson: Gson): StringListConverter =
        StringListConverter(gson)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        stringListConverter: StringListConverter
    ): CountriesDatabase =
        Room.databaseBuilder(context, CountriesDatabase::class.java, "countries.db")
            .addTypeConverter(stringListConverter)
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideCountryDao(db: CountriesDatabase): CountryDao = db.countryDao()

    @Provides
    fun provideCountryDetailDao(db: CountriesDatabase): CountryDetailDao = db.countryDetailDao()

    @Provides
    fun provideRemoteKeyDao(db: CountriesDatabase): RemoteKeyDao = db.remoteKeyDao()
}
