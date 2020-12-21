package com.xfhy.allinone.ipc.messenger

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import com.xfhy.library.ext.log

//官方文档上的这种常量都是定义在这里的,效仿着也这么写.
//private const val MSG_SAY_HELLO = 1
private const val TAG = "xfhy_messenger"

/**
 * Messenger 远程Service
 */
class MessengerService : Service() {

    private lateinit var mMessenger: Messenger

    override fun onBind(intent: Intent): IBinder {
        log(TAG, "onBind~")
        //传入Handler实例化Messenger
        mMessenger = Messenger(IncomingHandler(this))
        //将Messenger中的binder返回给客户端,让它可以远程调用
        return mMessenger.binder
    }

    //处理客户端传递过来的消息(Message)  并根据what决定下一步操作
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler(
        Looper.getMainLooper()
    ) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO -> {
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show()
                    log(TAG, "hello!")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

}
