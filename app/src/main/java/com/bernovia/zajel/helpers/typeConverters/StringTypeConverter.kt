package com.bernovia.zajel.helpers.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringTypeConverter {
    private val gson = Gson()

    @TypeConverter fun stringToList(data: String?): List<String> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter fun ListToString(someObjects: List<String?>?): String {
        return gson.toJson(someObjects)
    }

}