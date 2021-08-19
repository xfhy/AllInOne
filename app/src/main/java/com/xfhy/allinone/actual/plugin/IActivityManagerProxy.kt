package com.xfhy.allinone.actual.plugin

import android.content.Intent
import org.jetbrains.anko.collections.forEachWithIndex
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author : xfhy
 * Create time : 2021/8/18 9:42 下午
 * Description : IActivityManager 代理
 */
class IActivityManagerProxy(private val mActivityManager: Any) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method, args: Array<Any>?): Any? {
        //如果IActivityManager的startActivity被调用
        if ("startActivity" == method.name) {
            var intent: Intent? = null
            var index = 0
            args?.forEachWithIndex { i, any ->
                if (any is Intent) {
                    intent = any
                    index = i
                }
            }
            //取出参数中的Intent中的包名  判断请求的Activity是否是插件包里面的
            intent?.component?.className?.contains("com.xfhy.pluginapkdemo")?.let {
                //如果是,那么临时将目标Activity改为占坑Activity StubActivity
                val subIntent = Intent()
                val packageName = "com.xfhy.allinone.actual.plugin"
                subIntent.setClassName(packageName, "$packageName.StubActivity")
                //记录下来,待会儿方便 打开这个Activity
                subIntent.putExtra(HookHelper.TARGET_INTENT, intent)
                //Intent改好之后,还回去
                args?.set(index, subIntent)
            }
        }
        if (args != null) {
            return method.invoke(mActivityManager, *args)
        }
        return method.invoke(mActivityManager, null)
    }

}