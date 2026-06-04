package dev.gustavo.countries.data.remote.model

import com.google.gson.annotations.SerializedName

data class CountryRemote(
    @SerializedName("cca3") val cca3: String?,
    @SerializedName("name") val name: NameRemote?,
    @SerializedName("capital") val capital: List<String>?,
    @SerializedName("flags") val flags: FlagsRemote?,
    @SerializedName("region") val region: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("languages") val languages: Map<String, String>?,
    @SerializedName("population") val population: Long?,
    @SerializedName("borders") val borders: List<String>?,
    @SerializedName("currencies") val currencies: Map<String, CurrencyRemote>?
)

data class NameRemote(
    @SerializedName("common") val common: String?,
    @SerializedName("official") val official: String?
)

data class FlagsRemote(
    @SerializedName("png") val png: String?,
    @SerializedName("svg") val svg: String?,
    @SerializedName("alt") val alt: String?
)

data class CurrencyRemote(
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?
)
