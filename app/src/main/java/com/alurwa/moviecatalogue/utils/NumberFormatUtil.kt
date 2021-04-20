package com.alurwa.moviecatalogue.utils

import java.text.DecimalFormat

object NumberFormatUtil {
    fun withComma(value: Int): String = DecimalFormat("#,###").format(value)

    fun timeHoursAndMinute(time: Int): String {
        var result = ""
        val ba = (time / 60)
        result = if (time % 60 == 0 && time >= 60) {
            "$ba j "
        } else if (time < 60) {
            "$time m"
        } else {
            "$ba j "+(time % 60)+" m"
        }

        return result
    }
}