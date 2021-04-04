package com.xfhy.allinone.performance.caton

import android.content.Context
import android.os.Build
import android.view.Choreographer
import android.view.WindowManager
import com.xfhy.allinone.App


/**
 * @author : xfhy
 * Create time : 2021/4/5 6:26 AM
 * Description :
 */

object ChoreographerMonitor {
    @Volatile
    private var isStart = false
    private val monitorFrameCallback by lazy { MonitorFrameCallback() }
    private var mListener: (Int) -> Unit = {}
    private var mLastTime = 0L

    fun startMonitor(listener: (Int) -> Unit) {
        if (isStart) {
            return
        }
        mListener = listener
        Choreographer.getInstance().postFrameCallback(monitorFrameCallback)
        isStart = true
    }

    fun stopMonitor() {
        isStart = false
        Choreographer.getInstance().removeFrameCallback { monitorFrameCallback }
    }

    class MonitorFrameCallback : Choreographer.FrameCallback {

        private val refreshRate by lazy {
            //计算刷新率 赋值给refreshRate
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                App.getAppContext().display?.refreshRate ?: 16.6f
            } else {
                val windowManager =
                    App.getAppContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.defaultDisplay.refreshRate
            }
        }

        override fun doFrame(frameTimeNanos: Long) {
            mLastTime = if (mLastTime == 0L) {
                frameTimeNanos
            } else {
                //frameTimeNanos的单位是纳秒,这里需要计算时间差,然后转成毫秒
                val time = (frameTimeNanos - mLastTime) / 1000000
                //跳过了多少帧
                val frames = (time / (1000f / refreshRate)).toInt()
                if (frames > 1) {
                    mListener.invoke(frames)
                }
                frameTimeNanos
            }
            Choreographer.getInstance().postFrameCallback(this)
        }

    }
}
