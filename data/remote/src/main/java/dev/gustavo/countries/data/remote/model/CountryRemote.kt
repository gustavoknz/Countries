package dev.gustavo.countries.data.remote.model

import com.google.gson.annotations.SerializedName

data class CountryRemote(
    @SerializedName("codes") val codes: CodesRemote?,
    @SerializedName("names") val names: NameRemote?,
    @SerializedName("capitals") val capitals: List<CapitalRemote>?,
    @SerializedName("flag") val flag: FlagRemote?,
    @SerializedName("region") val region: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("languages") val languages: List<LanguageRemote>?,
    @SerializedName("population") val population: Long?,
    @SerializedName("borders") val borders: List<String>?,
    @SerializedName("currencies") val currencies: List<CurrencyRemote>?,
    @SerializedName("classification") val classification: ClassificationRemote?
)

data class CodesRemote(
    @SerializedName("alpha_3") val alpha3: String?
)

data class NameRemote(
    @SerializedName("common") val common: String?,
    @SerializedName("official") val official: String?
)

data class CapitalRemote(
    @SerializedName("name") val name: String?
)

data class FlagRemote(
    @SerializedName("url_png") val png: String?,
    @SerializedName("url_svg") val svg: String?
)

data class LanguageRemote(
    @SerializedName("name") val name: String?
)

data class CurrencyRemote(
    @SerializedName("name") val name: String?
)

data class ClassificationRemote(
    @SerializedName("dependency") val dependency: Boolean?
)
