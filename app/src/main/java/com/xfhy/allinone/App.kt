package com.xfhy.allinone

import android.content.Context
import com.xfhy.allinone.actual.plugin.HookHelper
import com.xfhy.library.common.BaseApplication
import me.weishu.reflection.Reflection

/**
 * @author : xfhy
 * Create time : 2020/11/27 7:34 AM
 * Description :
 */
class App : BaseApplication() {

    companion object {
        fun getAppContext() = context
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            HookHelper.hookAMS()
            HookHelper.hookHandler()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Reflection.unseal(base)
    }

    override fun onCreate() {
        super.onCreate()
        //HotFixUtil.loadPatch()
    }

}