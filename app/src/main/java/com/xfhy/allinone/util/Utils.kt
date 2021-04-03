package com.xfhy.allinone.util

import android.os.Looper

/**
 * @author : xfhy
 * Create time : 2021/4/3 7:47 AM
 * Description :
 */
fun getMainThreadStackTrace(): String {
    val stackTrace = Looper.getMainLooper().thread.stackTrace
    return StringBuilder().apply {
        for (stackTraceElement in stackTrace) {
            append(stackTraceElement.toString())
            append("\n")
        }
    }.toString()
}