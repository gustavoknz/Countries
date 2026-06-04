package dev.gustavo.countries.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun toList(json: String): List<String> =
        gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
}
