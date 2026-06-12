package dev.gustavo.countries.data.remote.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data") val data: DataWrapper<T>?
)

data class DataWrapper<T>(
    @SerializedName("objects") val objects: List<T>?
)
