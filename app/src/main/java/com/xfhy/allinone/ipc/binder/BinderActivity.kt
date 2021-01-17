package com.xfhy.allinone.ipc.binder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_binder.*

/**
 * Binder test
 */
private const val TAG = "xfhy_binder"

class BinderActivity : TitleBarActivity() {

    private var mService: IBinder? = null

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = service
            log(TAG,"Client : onServiceConnected  $service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun getThisTitle(): CharSequence {
        return "Binder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder)


        btnTransferBigData.setOnClickListener {
            transferBigData()
        }
        btnBindBinderService.setOnClickListener {
            bindBinderService()
        }
        btnTestSupport.setOnClickListener {
            testSupportException()
        }
        btnTestUnSupport.setOnClickListener {
            testUnSupportException()
        }
        btnTestError.setOnClickListener {
            testError()
        }
    }

    private fun bindBinderService() {
        Intent().apply {
            action = "com.xfhy.binder.Server.Action"
            setPackage("com.xfhy.allinone")
        }.also { intent ->
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun testSupportException() {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            mService?.transact(TEST_SUPPORT_EXCEPTION, data, reply, 0)
            reply.readException()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

    private fun testUnSupportException() {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            mService?.transact(TEST_UNSUPPORT_EXCEPTION, data, reply, 0)
            reply.readException()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

    private fun testError() {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            mService?.transact(TEST_ERROR, data, reply, 0)
            reply.readException()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

    /**
     * Binder跨进程传递大数据问题
     * Intent传递大数据->发送广播->app与AMS通信(跨进程),所以这里传递大数据会崩溃.触发到了Binder的最大内存限制问题.
     */
    private fun transferBigData() {

        /*
        * 2021-01-13 07:10:34.217 20303-20303/com.xfhy.allinone E/JavaBinder: !!! FAILED BINDER TRANSACTION !!!  (parcel size = 4194612)
        2021-01-13 07:10:34.219 20303-20303/com.xfhy.allinone E/AndroidRuntime: FATAL EXCEPTION: main
        Process: com.xfhy.allinone, PID: 20303
    java.lang.RuntimeException: android.os.TransactionTooLargeException: data parcel size 4194612 bytes
        at android.app.ContextImpl.sendBroadcast(ContextImpl.java:886)
        at android.content.ContextWrapper.sendBroadcast(ContextWrapper.java:421)
        at android.content.ContextWrapper.sendBroadcast(ContextWrapper.java:421)
        at com.xfhy.allinone.ipc.binder.BinderActivity.transferBigData(BinderActivity.kt:37)
        at com.xfhy.allinone.ipc.binder.BinderActivity.access$transferBigData(BinderActivity.kt:16)
        at com.xfhy.allinone.ipc.binder.BinderActivity$onCreate$1.onClick(BinderActivity.kt:28)
        at android.view.View.performClick(View.java:5637)
        at android.view.View$PerformClick.run(View.java:22429)
        at android.os.Handler.handleCallback(Handler.java:751)
        at android.os.Handler.dispatchMessage(Handler.java:95)
        at android.os.Looper.loop(Looper.java:154)
        at android.app.ActivityThread.main(ActivityThread.java:6119)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)
     Caused by: android.os.TransactionTooLargeException: data parcel size 4194612 bytes
        at android.os.BinderProxy.transactNative(Native Method)
        at android.os.BinderProxy.transact(Binder.java:615)
        at android.app.ActivityManagerProxy.broadcastIntent(ActivityManagerNative.java:3536)
        at android.app.ContextImpl.sendBroadcast(ContextImpl.java:881)
        at android.content.ContextWrapper.sendBroadcast(ContextWrapper.java:421) 
        at android.content.ContextWrapper.sendBroadcast(ContextWrapper.java:421) 
        at com.xfhy.allinone.ipc.binder.BinderActivity.transferBigData(BinderActivity.kt:37) 
        at com.xfhy.allinone.ipc.binder.BinderActivity.access$transferBigData(BinderActivity.kt:16) 
        at com.xfhy.allinone.ipc.binder.BinderActivity$onCreate$1.onClick(BinderActivity.kt:28) 
        at android.view.View.performClick(View.java:5637) 
        at android.view.View$PerformClick.run(View.java:22429) 
        at android.os.Handler.handleCallback(Handler.java:751) 
        at android.os.Handler.dispatchMessage(Handler.java:95) 
        at android.os.Looper.loop(Looper.java:154) 
        at android.app.ActivityThread.main(ActivityThread.java:6119) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776) 
        * */

        Intent().apply {
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val createScaledBitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, false)
            putExtra("byte_data", createScaledBitmap)
            sendBroadcast(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

}
