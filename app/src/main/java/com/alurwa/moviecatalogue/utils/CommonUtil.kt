package com.alurwa.moviecatalogue.utils

import android.content.Context
import java.util.*

object CommonUtil {

    /**
     * Get Language name from 2 digit language
     **/
    fun getLanguageName(language: String?): String? {
        return Locale.getAvailableLocales().find {
            it.language == language
        }.let {
            it?.getDisplayLanguage(Locale.ENGLISH)
        }
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
    }
}