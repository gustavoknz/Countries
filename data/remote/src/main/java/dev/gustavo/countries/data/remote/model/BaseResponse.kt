package dev.gustavo.countries.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("data") val data: DataWrapper<T>? = null
)

@Serializable
data class DataWrapper<T>(
    @SerialName("objects") val objects: List<T>? = null,
    @SerialName("meta") val meta: MetaRemote? = null
)

@Serializable
data class MetaRemote(
    @SerialName("total") val total: Int? = null,
    @SerialName("count") val count: Int? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("offset") val offset: Int? = null,
    @SerialName("more") val more: Boolean? = null
)
