package com.xfhy.allinone.util

import androidx.annotation.Keep
import com.xfhy.library.ext.log
import com.xfhy.allinone.util.TimeCache
import java.util.HashMap

@Keep
object TimeCache {
    private var mStartTimes: MutableMap<String, Long> = HashMap()
    private var mEndTimes: MutableMap<String, Long> = HashMap()

    @JvmStatic
    fun putStartTime(methodName: String, className: String) {
        mStartTimes["$methodName,$className"] = System.currentTimeMillis()
    }

    @JvmStatic
    fun putEndTime(methodName: String, className: String) {
        mEndTimes["$methodName,$className"] = System.currentTimeMillis()
        printlnTime(methodName, className)
    }

    @JvmStatic
    fun printlnTime(methodName: String, className: String) {
        val key = "$methodName,$className"
        if (!mStartTimes.containsKey(key) || !mEndTimes.containsKey(key)) {
            log("className =" + key + "not exist")
        }
        val currTime = mEndTimes[key]!! - mStartTimes[key]!!
        log("className =$className methodName =$methodName，time consuming $currTime  ms")
    }
}