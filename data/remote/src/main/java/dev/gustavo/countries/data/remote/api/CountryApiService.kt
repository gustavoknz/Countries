package dev.gustavo.countries.data.remote.api

import dev.gustavo.countries.data.remote.model.CountryRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryApiService {

    @GET("all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,capital,currencies,flags,region,cca3,independent"
    ): List<CountryRemote>

    @GET("alpha/{cca3}")
    suspend fun getCountryDetail(
        @Path("cca3") cca3: String
    ): List<CountryRemote>
}
