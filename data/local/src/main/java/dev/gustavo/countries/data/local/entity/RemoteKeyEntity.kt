package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextKey: Int?
) {
    companion object {
        const val COUNTRIES_LIST_ID = "countries_list"
        fun getListId(query: String?): String = query?.let { "search_$it" } ?: COUNTRIES_LIST_ID
    }
}
