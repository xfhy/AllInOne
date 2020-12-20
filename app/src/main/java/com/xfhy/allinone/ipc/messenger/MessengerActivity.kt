package com.xfhy.allinone.ipc.messenger

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * Messenger test
 */
private const val TAG = "xfhy_messenger"

class MessengerActivity : TitleBarActivity() {

    /** 与服务端进行沟通的Messenger */
    private var mService: Messenger? = null

    /** 是否已bindService */
    private var bound: Boolean = false

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

    }

}
