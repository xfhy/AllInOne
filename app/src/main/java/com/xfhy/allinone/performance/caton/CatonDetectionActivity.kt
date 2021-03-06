package com.xfhy.allinone.performance.caton

import android.os.Bundle
import android.os.Looper
import com.xfhy.allinone.databinding.ActivityCatonDetectionBinding
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2021/4/3 6:40 AM
 * Description : 卡顿检测
 * 博客-Choreographer: https://github.com/xfhy/Android-Notes/tree/master/Blogs/Android/%E7%B3%BB%E7%BB%9F%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90
 */
class CatonDetectionActivity : TitleBarActivity() {

    private lateinit var catonDetectionBinding: ActivityCatonDetectionBinding
    override fun getThisTitle() = "卡顿检测"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catonDetectionBinding = ActivityCatonDetectionBinding.inflate(layoutInflater)
        setContentView(catonDetectionBinding.root)

        catonDetectionBinding.btnDetectCatonLooper.setOnClickListener {
            //在Looper的loop方法中,消息分发的前后会通过外部设置进去的Printer对象进行日志打印,我们构建一个Printer传递给Looper,然后根据这个Printer
            //打印的日志就知道什么时候开始分发消息,什么时候分发消息结束.当开始分发消息与结束分发消息时间间隔太大时,就认为是发生卡顿了,这个时候取线程堆栈信息,
            //这种匹配字符串方案能够准确地在发生卡顿时拿到堆栈信息,但有一定的性能损耗,不适合线上监控.
            startLooperMonitor()
        }
        catonDetectionBinding.btnDetectCatonChoreographer.setOnClickListener {
            startChoreographerMonitor()
        }
        catonDetectionBinding.btnCaton.setOnClickListener {
            manufacturingCaton()
        }
    }

    private fun startChoreographerMonitor() {
        //UI绘图的操作没有跟上VSYNC信号,步调不一致,绘图的过程中操作太多,导致操作的时间超过16.6ms.卡顿时,可能存在VSYNC信号来了好几次,结果只绘制了一次的情况.
        //如果超过3次,用户就有视觉上的感知.
        ChoreographerMonitor.startMonitor {
            log("丢帧 $it ")
        }
    }

    private fun manufacturingCaton() {
        Thread.sleep(4000)
    }

    private fun startLooperMonitor() {
        val looperPrinter = LooperPrinter()
        Looper.getMainLooper().setMessageLogging(looperPrinter)
    }

    override fun onDestroy() {
        super.onDestroy()
        ChoreographerMonitor.stopMonitor()
    }

}