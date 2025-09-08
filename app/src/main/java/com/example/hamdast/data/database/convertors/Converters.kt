package com.example.hamdast.data.database.convertors

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListInt(value: List<Int>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toListInt(value: String): List<Int> {
        return if (value.isEmpty()) emptyList()
        else value.split(",").map { it.toInt() }
    }
}
