package com.alurwa.moviecatalogue.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

/**
 * Created by Purwa Shadr Al 'urwa on 14/05/2021
 */

object NetworkState {

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork
            activeNetwork != null && cm.getNetworkCapabilities(activeNetwork) != null
        } else {
            val networkInfo = cm.activeNetworkInfo
            networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}