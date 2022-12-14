package com.xfhy.allinone.performance.memory

import android.app.ActivityManager
import android.content.Context
import com.xfhy.allinone.base.BaseDemoActivity
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/12/15 7:18
 * Description : 内存优化
 */
class MemoryOptActivity : BaseDemoActivity() {
    override fun getThisTitle(): CharSequence {
        return "内存优化"
    }

    override fun initButtons() {
        addButtonItem("获取内存数据", ::getMemoryData)
    }

    private fun getMemoryData() {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(memoryInfo)
        log("$memoryInfo")

        /*
        获取出来的结果：
        availMem = 2397519872  系统上的可用内存。这个数字不应该被认为是绝对的：由于内核的性质，这个内存的很大一部分实际上在使用中，需要整个系统正常运行
        lowMemory = false  如果系统认为当前内存不足，则设置为true。
        threshold = 226492416  availMem的阈值，在该阈值下，我们认为内存不足，并开始终止后台服务和其他无关进程。
        totalMem = 5941764096  内核可访问的总内存。这基本上是设备的RAM大小，不包括低于内核的固定分配，如DMA缓冲区、基带CPU的RAM等。
        * */

    }
}