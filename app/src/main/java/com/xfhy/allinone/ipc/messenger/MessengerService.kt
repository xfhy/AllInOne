package com.xfhy.allinone.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
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
        mMessenger = Messenger(IncomingHandler())
        //将Messenger中的binder返回给客户端,让它可以远程调用
        return mMessenger.binder
    }

    //处理客户端传递过来的消息(Message)  并根据what决定下一步操作
    internal class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO -> {
                    log(TAG, "hello!")
                    //客户端的Messenger就是放在Message的replyTo中的
                    replyToClient(msg, "I have received your message and will reply to you later")
                }
                MSG_TRANSFER_SERIALIZABLE -> log(TAG, "传递过来的对象:  ${msg.data?.get("person")}")
                else -> super.handleMessage(msg)
            }
        }

        private fun replyToClient(msg: Message, replyText: String) {
            val clientMessenger = msg.replyTo
            val replyMessage = Message.obtain(null, MSG_FROM_SERVICE)
            replyMessage.data = Bundle().apply {
                putString("reply", replyText)
            }
            try {
                clientMessenger?.send(replyMessage)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

}
