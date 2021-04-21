package com.alurwa.moviecatalogue.utils

import java.util.*

object CommonUtil {

    /**
     * Get Language name from 2 digit language
     **/
    fun getLanguageName(language: String): String? {
        return Locale.getAvailableLocales().find {
            it.language == language
        }.let {
            it?.getDisplayLanguage(Locale.ENGLISH)
        }
    }
}