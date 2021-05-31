/**
 * MIT License
 *
 * Copyright (c) 2021 Purwa Shadr Al 'urwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.TypeConverter
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.Genre
import com.alurwa.moviecatalogue.core.model.Season
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

    @TypeConverter
    fun fromNumber(value: Number): Double {
        return value.toDouble()
    }

    @TypeConverter
    fun toNumber(value: Double): Number {
        return value
    }

    @TypeConverter
    fun fromSeasons(value: List<Season>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Season>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSeasons(value: String): List<Season> {
        val gson = Gson()
        val type = object : TypeToken<List<Season>>() {}.type
        return gson.fromJson(value, type)
    }
}
