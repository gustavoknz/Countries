package dev.gustavo.countries.data.local.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

@ProvidedTypeConverter
class StringListConverter @Inject constructor(
    private val gson: Gson
) {

    @TypeConverter
    fun fromList(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun toList(json: String): List<String> {
        return if (json.isBlank()) {
            emptyList()
        } else {
            try {
                gson.fromJson(json, listType)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    companion object {
        private val listType: Type = object : TypeToken<List<String>>() {}.type
    }
}
