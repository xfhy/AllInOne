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
 * Description : Ashmem 服务端Service
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
                    * 2021-01-16 08:03:43.988 6352-6365/com.xfhy.allinone E/JavaBinder: *** Uncaught remote exception!  (Exceptions are not yet supported across processes.)
                        java.lang.RuntimeException
                            at com.xfhy.allinone.ipc.binder.BinderService$TestBinder.onTransact(BinderService.kt:32)
                            at android.os.Binder.execTransact(Binder.java:565)
                    * */
                    throw RuntimeException()
                }
                TEST_ERROR -> {
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