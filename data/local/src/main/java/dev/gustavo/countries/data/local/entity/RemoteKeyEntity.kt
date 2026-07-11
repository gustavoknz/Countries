package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        const val COUNTRIES_LIST_ID = "countries_list"
        fun getListId(query: String?, region: String?): String {
            return when {
                query == null && region == null -> COUNTRIES_LIST_ID
                query != null && region == null -> "search_$query"
                query == null && region != null -> "region_$region"
                else -> "search_${query}_region_$region"
            }
        }
    }
}
