package com.xfhy.allinone.ipc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_messenger.*
import java.io.Serializable

/**
 * Messenger test
 */
private const val TAG = "xfhy_messenger"

class MessengerActivity : TitleBarActivity() {

    /** 与服务端进行沟通的Messenger */
    private var mService: Messenger? = null

    /** 是否已bindService */
    private var bound: Boolean = false

    /** 客户端这边的Messenger */
    private var mClientMessenger = Messenger(IncomingHandler())

    class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FROM_SERVICE -> {
                    log(TAG, "Received from service: ${msg.data?.getString("reply")}")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            bound = false
        }
    }

    override fun getThisTitle(): CharSequence {
        return "Messenger"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        btnConnect.setOnClickListener {
            connectService()
        }
        btnSayHello.setOnClickListener {
            sayHello()
        }
        btnTransferSerializable.setOnClickListener {
            transferSerializable()
        }
    }

    private fun connectService() {
        Intent().apply {
            action = "com.xfhy.messenger.Server.Action"
            setPackage("com.xfhy.allinone")
        }.also { intent ->
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    //客户端调服务端方法时,需要捕获以下几个异常:
    //RemoteException 异常：
    //DeadObjectException 异常：连接中断时会抛出异常；
    //SecurityException 异常：客户端和服务端中定义的 AIDL 发生冲突时会抛出异常；
    private fun sayHello() {
        if (!bound) {
            return
        }
        //创建,并且发送一个message给服务端   Message中what指定为MSG_SAY_HELLO
        val message = Message.obtain(null, MSG_SAY_HELLO, 0, 0)
        message.replyTo = mClientMessenger
        message.data = Bundle().apply {
            putSerializable("person", SerializablePerson("张三"))
        }
        try {
            mService?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    data class SerializablePerson(val name: String) : Serializable

    private fun transferSerializable() {
        //这里为了简化代码方便查看核心,就不写是否绑定、捕获异常之类的代码了
        //创建,并且发送一个message给服务端   Message中what指定为MSG_TRANSFER_SERIALIZABLE
        val message = Message.obtain(null, MSG_TRANSFER_SERIALIZABLE, 0, 0)
        message.data = Bundle().apply {
            putSerializable("person", SerializablePerson("张三"))
        }
        mService?.send(message)
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(mServiceConnection)
            bound = false
        }
    }

}
