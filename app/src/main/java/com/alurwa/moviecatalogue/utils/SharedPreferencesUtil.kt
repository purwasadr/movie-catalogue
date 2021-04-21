package com.alurwa.moviecatalogue.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SharedPreference.APP_KEY, Context.MODE_PRIVATE)
    }

    fun getIsShowPosterPreferences(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean("is_show_poster", false)
    }

}