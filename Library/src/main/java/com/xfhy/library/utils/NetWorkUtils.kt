package com.xfhy.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.content.Context.CONNECTIVITY_SERVICE
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService


/**
 * Created by xfhy on 2018/2/4 17:03
 * Description :
 */
object NetWorkUtils {
    /**
     * 判断网络是否可用
     */
    @SuppressLint("MissingPermission")
    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 检测wifi是否连接
     */
    @SuppressLint("MissingPermission")
    fun isWifiConnected(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.allNetworks
            for (a in networkInfo.indices) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo[a])
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                }
            }
            return false
        } else {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }
}