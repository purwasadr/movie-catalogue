package com.alurwa.moviecatalogue.utils

import java.text.DecimalFormat

object NumberFormatUtil {
    fun withComma(value: Int): String = DecimalFormat("#,###").format(value)
}