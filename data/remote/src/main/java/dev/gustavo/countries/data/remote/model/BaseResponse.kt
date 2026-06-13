package dev.gustavo.countries.data.remote.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data") val data: DataWrapper<T>?
)

data class DataWrapper<T>(
    @SerializedName("objects") val objects: List<T>?,
    @SerializedName("meta") val meta: MetaRemote?
)

data class MetaRemote(
    @SerializedName("total") val total: Int?,
    @SerializedName("count") val count: Int?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("more") val more: Boolean?
)
