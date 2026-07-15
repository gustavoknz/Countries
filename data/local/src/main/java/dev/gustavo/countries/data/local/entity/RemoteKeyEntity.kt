package dev.gustavo.countries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.gustavo.countries.core.common.Region

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        const val COUNTRIES_LIST_ID = "countries_list"
        fun getListId(query: String?, region: Region?): String {
            val regionValue = region?.apiValue
            return when {
                query == null && regionValue == null -> COUNTRIES_LIST_ID
                query != null && regionValue == null -> "search_$query"
                query == null && regionValue != null -> "region_$regionValue"
                else -> "search_${query}_region_$regionValue"
            }
        }
    }
}
