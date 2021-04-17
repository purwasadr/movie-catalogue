package com.alurwa.moviecatalogue.core.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val SHARED_PREFERENCES_KEY = "movie_catalogue"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun getIsShowPosterPreferences(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean("is_show_poster", false)
    }

}