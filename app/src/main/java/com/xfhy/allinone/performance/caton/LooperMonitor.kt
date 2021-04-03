package com.xfhy.allinone.performance.caton

import android.util.Printer
import com.blankj.utilcode.util.ThreadUtils
import com.xfhy.allinone.util.getMainThreadStackTrace
import com.xfhy.library.ext.log
import java.util.concurrent.TimeUnit

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

    var mLastBeginTime = 0L
    var mLastEndTime = 0L
    var mHasEnd = false

    override fun println(msg: String?) {
        if (msg.isNullOrEmpty()) {
            return
        }
        log(TAG, "$msg")
        if (msg.startsWith(BEGIN_TAG)) {
            mHasEnd = false
            mLastBeginTime = System.currentTimeMillis()

            //需要单独搞个线程来获取堆栈
            //todo xfhy 需要停止
            ThreadUtils.executeBySingleWithDelay(object : ThreadUtils.Task<Unit>() {
                override fun doInBackground() {
                    if (!mHasEnd) {
                        log(TAG, getMainThreadStackTrace())
                    }
                }

                override fun onSuccess(result: Unit?) {

                }

                override fun onCancel() {

                }

                override fun onFail(t: Throwable?) {

                }
            }, DEFAULT_BLOCK_THRESHOLD_MILLIS, TimeUnit.MILLISECONDS)
        } else {
            mLastEndTime = System.currentTimeMillis()
            mHasEnd = true
            /*if (mLastEndTime - mLastBeginTime > DEFAULT_BLOCK_THRESHOLD_MILLIS) {
                //获取堆栈信息
                log(TAG, getStackTrace())
            }*/
        }
    }


}
