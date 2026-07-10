package dev.gustavo.countries.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryRemote(
    @SerialName("codes") val codes: CodesRemote? = null,
    @SerialName("names") val names: NameRemote? = null,
    @SerialName("capitals") val capitals: List<CapitalRemote>? = null,
    @SerialName("flag") val flag: FlagRemote? = null,
    @SerialName("region") val region: String? = null,
    @SerialName("subregion") val subregion: String? = null,
    @SerialName("languages") val languages: List<LanguageRemote>? = null,
    @SerialName("population") val population: Long? = null,
    @SerialName("borders") val borders: List<String>? = null,
    @SerialName("currencies") val currencies: List<CurrencyRemote>? = null,
    @SerialName("classification") val classification: ClassificationRemote? = null
)

@Serializable
data class CodesRemote(
    @SerialName("alpha_3") val alpha3: String? = null
)

@Serializable
data class NameRemote(
    @SerialName("common") val common: String? = null,
    @SerialName("official") val official: String? = null
)

@Serializable
data class CapitalRemote(
    @SerialName("name") val name: String? = null
)

@Serializable
data class FlagRemote(
    @SerialName("url_png") val png: String? = null,
    @SerialName("url_svg") val svg: String? = null
)

@Serializable
data class LanguageRemote(
    @SerialName("name") val name: String? = null
)

@Serializable
data class CurrencyRemote(
    @SerialName("name") val name: String? = null
)

@Serializable
data class ClassificationRemote(
    @SerialName("dependency") val dependency: Boolean? = null
)
