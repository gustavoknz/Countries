package dev.gustavo.countries.data.remote.api

import dev.gustavo.countries.core.common.Constants.DETAIL_RESPONSE_FIELDS
import dev.gustavo.countries.core.common.Constants.LIST_RESPONSE_FIELDS
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CountryRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryApiService {

    @GET("countries/v5")
    suspend fun getAllCountries(
        @Query("response_fields", encoded = true) fields: String = LIST_RESPONSE_FIELDS
    ): BaseResponse<CountryRemote>

    @GET("countries/v5/codes.alpha_3/{alpha3}")
    suspend fun getCountryDetail(
        @Path("alpha3") alpha3: String,
        @Query("response_fields", encoded = true) fields: String = DETAIL_RESPONSE_FIELDS
    ): BaseResponse<CountryRemote>
}
