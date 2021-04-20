package com.alurwa.moviecatalogue.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

object KeyboardUtil {
    fun hideKeyboard(context: Context, focus: View?) {
        if (focus != null) {
            (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }
}