package com.xfhy.allinone.ipc.binder

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import com.xfhy.library.ext.log


/**
 * @author : xfhy
 * Create time : 2021年01月14日07:27:44
 * Description : Binder 服务端Service
 */

private const val TAG = "xfhy_ashmem"
const val TEST_SUPPORT_EXCEPTION = 1
const val TEST_UNSUPPORT_EXCEPTION = 2
const val TEST_ERROR = 3

class BinderService : Service() {

    class TestBinder : Binder() {
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            log(TAG, "Server : onTransact code=$code")
            return when (code) {
                TEST_SUPPORT_EXCEPTION -> {
                    throw NullPointerException()
                }
                TEST_UNSUPPORT_EXCEPTION -> {
                    /*
                    *  08:03:43.988 6352-6365/com.xfhy.allinone E/JavaBinder: *** Uncaught remote exception!  (Exceptions are not yet supported across processes.)
                        java.lang.RuntimeException
                            at com.xfhy.allinone.ipc.binder.BinderService$TestBinder.onTransact(BinderService.kt:32)
                            at android.os.Binder.execTransact(Binder.java:565)
                    * */
                    throw RuntimeException()
                }
                TEST_ERROR -> {
                    /**
                     *  16:42:16.003 6983-6996/com.xfhy.allinone E/JavaBinder: *** Uncaught remote exception!  (Exceptions are not yet supported across processes.)
                                                            java.lang.NoSuchMethodError
                                                            at com.xfhy.allinone.ipc.binder.BinderService$TestBinder.onTransact(BinderService.kt:41)
                                                            at android.os.Binder.execTransact(Binder.java:565)

                     16:42:16.003 6983-6996/com.xfhy.allinone W/System.err: java.lang.NoSuchMethodError
                     16:42:16.004 6983-6996/com.xfhy.allinone W/System.err:     at com.xfhy.allinone.ipc.binder.BinderService$TestBinder.onTransact(BinderService.kt:41)
                     16:42:16.004 6983-6996/com.xfhy.allinone W/System.err:     at android.os.Binder.execTransact(Binder.java:565)
                     */
                    //这个会让服务端进程崩溃
                    /**
                     * 6925是客户端  6955服务端
                     *  16:39:17.533 6955-6968/com.xfhy.allinone E/JavaBinder: Forcefully exiting
                     16:39:17.582 1388-1388/? I/Zygote: Process 6955 exited cleanly (1)
                     16:39:17.588 1688-2849/? I/ActivityManager: Process com.xfhy.allinone:other (pid 6955) has died
                     16:39:17.588 1688-2849/? D/ActivityManager: cleanUpApplicationRecord -- 6955
                     16:39:17.589 1688-2849/? W/ActivityManager: Scheduling restart of crashed service com.xfhy.allinone/.ipc.binder.BinderService in 1000ms
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err: android.os.DeadObjectException
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at android.os.BinderProxy.transactNative(Native Method)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at android.os.BinderProxy.transact(Binder.java:615)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at com.xfhy.allinone.ipc.binder.BinderActivity.testError(BinderActivity.kt:102)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at com.xfhy.allinone.ipc.binder.BinderActivity.access$testError(BinderActivity.kt:21)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at com.xfhy.allinone.ipc.binder.BinderActivity$onCreate$5.onClick(BinderActivity.kt:57)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at android.view.View.performClick(View.java:5637)
                     16:39:17.591 6925-6925/com.xfhy.allinone W/System.err:     at android.view.View$PerformClick.run(View.java:22429)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at android.os.Handler.handleCallback(Handler.java:751)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:95)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at android.os.Looper.loop(Looper.java:154)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:6119)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at java.lang.reflect.Method.invoke(Native Method)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
                     16:39:17.592 6925-6925/com.xfhy.allinone W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)
                     */
                    throw NoSuchMethodError()
                }
                else -> super.onTransact(code, data, reply, flags)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        log(TAG, "Server : onBind")
        return TestBinder()
    }

}