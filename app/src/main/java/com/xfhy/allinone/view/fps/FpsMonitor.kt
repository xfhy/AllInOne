package com.xfhy.allinone.view.fps

import android.os.Handler
import android.os.Looper
import android.view.Choreographer

/**
 * @author : xfhy
 * Create time : 2021/4/2 10:26
 * Description : FPS 监测
 */
object FpsMonitor {

    private const val FPS_INTERVAL_TIME = 1000L

    /**
     * 1秒内执行回调的次数  即fps
     */
    private var count = 0
    private val mMonitorListeners = mutableListOf<(Int) -> Unit>()

    @Volatile
    private var isStartMonitor = false
    private val monitorFrameCallback by lazy { MonitorFrameCallback() }
    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    fun startMonitor(listener: (Int) -> Unit) {
        mMonitorListeners.add(listener)
        if (isStartMonitor) {
            return
        }
        isStartMonitor = true
        Choreographer.getInstance().postFrameCallback(monitorFrameCallback)
        //1秒后结算 count次数
        mainHandler.postDelayed(monitorFrameCallback, FPS_INTERVAL_TIME)
    }

    fun stopMonitor() {
        isStartMonitor = false
        count = 0
        Choreographer.getInstance().removeFrameCallback(monitorFrameCallback)
        mainHandler.removeCallbacks(monitorFrameCallback)
    }

    class MonitorFrameCallback : Choreographer.FrameCallback, Runnable {

        //VSYNC信号到了，且处理到当前异步消息了，才会回调这里
        override fun doFrame(frameTimeNanos: Long) {
            //次数+1  1秒内
            count++
            //继续下一次 监听VSYNC信号
            Choreographer.getInstance().postFrameCallback(this)
        }

        override fun run() {
            //将count次数传递给外面
            mMonitorListeners.forEach {
                it.invoke(count)
            }
            count = 0
            //继续发延迟消息  等到1秒后统计count次数
            mainHandler.postDelayed(this, FPS_INTERVAL_TIME)
        }
    }

}