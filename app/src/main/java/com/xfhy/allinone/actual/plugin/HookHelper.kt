package com.xfhy.allinone.actual.plugin

import android.os.Build
import com.xfhy.library.ext.log
import com.xfhy.library.utils.FieldUtil
import java.lang.reflect.Proxy

/**
 * @author : xfhy
 * Create time : 2021/8/18 9:54 下午
 * Description :
 */
object HookHelper {

    const val TARGET_INTENT = "target_intent"

    fun hookAMS() {
        val defaultSingleton = if (Build.VERSION.SDK_INT >= 26) {
            //拿到IActivityManager
            val activityManagerClazz = Class.forName("android.app.ActivityManager")
            FieldUtil.getFieldValue(activityManagerClazz, null, "IActivityManagerSingleton")
        } else {
            val activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative")
            //获取ActivityManagerNative中的gDefault字段
            FieldUtil.getFieldValue(activityManagerNativeClazz, null, "gDefault")
        }

        val SingletonClazz = Class.forName("android.util.Singleton")
        val mInstanceField = FieldUtil.getField(SingletonClazz, "mInstance")
        val IActivityManager = mInstanceField.get(defaultSingleton)
        log("获取到了IActivityManager")

        //设置动态代理
        val IActivityManagerClazz = Class.forName("android.app.IActivityManager")
        val proxy =
            Proxy.newProxyInstance(Thread.currentThread().contextClassLoader, arrayOf(IActivityManagerClazz), IActivityManagerProxy(IActivityManager))
        mInstanceField.set(defaultSingleton, proxy)
        log("设置动态代理IActivityManager成功")
    }

}