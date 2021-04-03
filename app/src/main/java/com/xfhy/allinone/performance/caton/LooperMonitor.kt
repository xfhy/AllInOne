package com.xfhy.allinone.performance.caton

import android.os.Handler
import android.os.HandlerThread
import android.util.Printer
import com.xfhy.allinone.util.getMainThreadStackTrace
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2021/4/3 7:10 AM
 * Description :
 */

const val TAG = "looper_monitor"

/**
 * 默认卡顿阈值
 */
const val DEFAULT_BLOCK_THRESHOLD_MILLIS = 3000L
const val BEGIN_TAG = ">>>>> Dispatching"
const val END_TAG = "<<<<< Finished"

class LooperPrinter : Printer {

    private var mBeginTime = 0L

    @Volatile
    var mHasEnd = false
    private val collectRunnable by lazy { CollectRunnable() }
    private val handlerThreadWrapper by lazy { HandlerThreadWrapper() }

    override fun println(msg: String?) {
        if (msg.isNullOrEmpty()) {
            return
        }
        log(TAG, "$msg")
        if (msg.startsWith(BEGIN_TAG)) {
            mBeginTime = System.currentTimeMillis()
            mHasEnd = false

            //需要单独搞个线程来获取堆栈
            handlerThreadWrapper.handler.postDelayed(
                collectRunnable,
                DEFAULT_BLOCK_THRESHOLD_MILLIS
            )
        } else {
            mHasEnd = true
            if (System.currentTimeMillis() - mBeginTime < DEFAULT_BLOCK_THRESHOLD_MILLIS) {
                handlerThreadWrapper.handler.removeCallbacks(collectRunnable)
            }
        }
    }

    inner class CollectRunnable : Runnable {
        override fun run() {
            if (!mHasEnd) {
                log(TAG, getMainThreadStackTrace())
            }
        }
    }

    class HandlerThreadWrapper {
        var handler: Handler
        init {
            val handlerThread = HandlerThread("LooperHandlerThread")
            handlerThread.start()
            handler = Handler(handlerThread.looper)
        }
    }

}

/*
* 输出
* java.lang.Thread.sleep(Native Method)
    java.lang.Thread.sleep(Thread.java:373)
    java.lang.Thread.sleep(Thread.java:314)
    com.xfhy.allinone.performance.caton.CatonDetectionActivity.manufacturingCaton(CatonDetectionActivity.kt:39)
    com.xfhy.allinone.performance.caton.CatonDetectionActivity.access$manufacturingCaton(CatonDetectionActivity.kt:14)
    com.xfhy.allinone.performance.caton.CatonDetectionActivity$onCreate$3.onClick(CatonDetectionActivity.kt:34)
    android.view.View.performClick(View.java:6597)
    android.view.View.performClickInternal(View.java:6574)
    android.view.View.access$3100(View.java:778)
    android.view.View$PerformClick.run(View.java:25885)
    android.os.Handler.handleCallback(Handler.java:873)
    android.os.Handler.dispatchMessage(Handler.java:99)
    android.os.Looper.loop(Looper.java:193)
    android.app.ActivityThread.main(ActivityThread.java:6669)
    java.lang.reflect.Method.invoke(Native Method)
    com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
    com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
* */