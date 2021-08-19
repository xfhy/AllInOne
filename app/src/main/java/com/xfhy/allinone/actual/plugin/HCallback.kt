package com.xfhy.allinone.actual.plugin

import android.content.Intent
import android.os.Handler
import android.os.Message
import com.xfhy.library.utils.FieldUtil


/**
 * @author : xfhy
 * Create time : 2021/8/19 9:42 下午
 * Description :
 */
const val LAUNCH_ACTIVITY = 100

class HCallback(private val handler: Handler) : Handler.Callback {

    //28以上是EXECUTE_TRANSACTION

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == LAUNCH_ACTIVITY) {
            val r = msg.obj
            try {
                //得到消息中的Intent(启动SubActivity的Intent)
                val intent = FieldUtil.getFieldValue(r.javaClass, r, "intent") as Intent
                //得到此前保存起来的Intent(启动TargetActivity的Intent)
                val target = intent.getParcelableExtra<Intent>(HookHelper.TARGET_INTENT)
                //为空  则不管,说明不是插件的Activity
                target?.let {
                    //将启动SubActivity的Intent替换成启动TargetActivity的Intent
                    intent.component = it.component
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        handler.handleMessage(msg)
        return true
    }
}