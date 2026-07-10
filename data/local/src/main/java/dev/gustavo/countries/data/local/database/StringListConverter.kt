package dev.gustavo.countries.data.local.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class StringListConverter @Inject constructor(
    private val json: Json
) {

    @TypeConverter
    fun fromList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toList(jsonString: String): List<String> {
        return if (jsonString.isBlank()) {
            emptyList()
        } else {
            try {
                json.decodeFromString(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
