package com.andresoller.device.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.File
import javax.inject.Inject

class NetworkUtils @Inject constructor(private val context: Context) {

    public fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun getCacheDir(): File {
        return context.cacheDir
    }

}