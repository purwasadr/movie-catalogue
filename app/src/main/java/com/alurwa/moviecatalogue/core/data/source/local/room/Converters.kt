package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.TypeConverter
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Purwa Shadr Al 'urwa on 14/05/2021
 */

class Converters {

    @TypeConverter
    fun fromGenres(value: List<Genre>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGenres(value: String): List<Genre> {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCastList(value: List<Cast>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Cast>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCastList(value: String): List<Cast> {
        val gson = Gson()
        val type = object : TypeToken<List<Cast>>() {}.type
        return gson.fromJson(value, type)
    }
}