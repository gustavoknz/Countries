package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
) {
    companion object {
        fun getListId(query: String?): String = query?.let { "search_$it" } ?: "countries_list"
    }
}
