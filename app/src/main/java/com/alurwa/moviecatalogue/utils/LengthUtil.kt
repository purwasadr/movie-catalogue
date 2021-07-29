package com.alurwa.moviecatalogue.utils

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),

        // Avoid using system resource
        context.resources.displayMetrics
    ).toInt()