package com.xfhy.allinone.actual.anrwatchdog

import android.app.ActivityManager
import android.app.ActivityManager.ProcessErrorStateInfo
import android.content.Context
import com.xfhy.allinone.App


/**
 * @author : xfhy
 * Create time : 2022/11/16 7:23
 * Description :
 */
object AnrWatchHelper {

    fun start() {
        ANRWatchDog()
            .setANRInterceptor {
                var activityService = App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE)

                1
            }
            .start()
    }

     fun checkAnr(): String {
        val activityManager = App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
         //todo xfhy 这里可能发生 RuntimeException or OOM
        val errorStateInfos = activityManager.processesInErrorState
        if (errorStateInfos != null) {
            for (info in errorStateInfos) {
                if (info.condition == ProcessErrorStateInfo.NOT_RESPONDING) {
                    val anrInfo = StringBuilder()
                    anrInfo.append(info.processName)
                        .append("\n")
                        .append(info.shortMsg)
                        .append("\n")
                        .append(info.longMsg)
                    return anrInfo.toString()
                }
            }
        }
        return ""
    }

}