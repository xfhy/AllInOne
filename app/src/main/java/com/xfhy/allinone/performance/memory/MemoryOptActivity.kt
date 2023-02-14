package com.xfhy.allinone.performance.memory

import android.app.ActivityManager
import android.content.Context
import com.xfhy.allinone.base.BaseDemoActivity
import com.xfhy.library.ext.log
import com.xfhy.nativelib.MonitorMalloc
import com.xfhy.nativelib.TestMalloc

/**
 * @author : xfhy
 * Create time : 2022/12/15 7:18
 * Description : 内存优化
 */
class MemoryOptActivity : BaseDemoActivity() {

    companion object {
        var aa:IntArray?=null
    }

    override fun getThisTitle(): CharSequence {
        return "内存优化"
    }

    override fun initButtons() {
        addButtonItem("获取内存数据", ::getMemoryData)
        addButtonItem("开始监测malloc内存申请", ::startMonitorMalloc)
        addButtonItem("native申请内存", ::nativeCreateMemory)
    }

    private fun startMonitorMalloc() {
        MonitorMalloc().start()
    }

    private fun nativeCreateMemory() {
        TestMalloc().testMalloc()
    }

    //1。线下获取内存数据
    /*
    * 1. adb shell
    * 2. ps -A | grep xfhy
      3. dumpsys meminfo 具体的pid

      * 第2步获取到的类似：u0_a181      29103   550 4491984  77952 0                   0 S com.xfhy.allinone
      * 第3步：dumpsys meminfo 29103

        ** MEMINFO in pid 29103 [com.xfhy.allinone] **
                   Pss  Private  Private  SwapPss     Heap     Heap     Heap
                 Total    Dirty    Clean    Dirty     Size    Alloc     Free
                ------   ------   ------   ------   ------   ------   ------
  Native Heap     8726     8648        0       21    53248    49231     4016
  Dalvik Heap     2410     1936        0       15     7707     1563     6144
 Dalvik Other      754      732        0        0
        Stack       40       40        0        0
       Ashmem        2        0        0        0
    Other dev       12        0       12        0
     .so mmap     1162      212        0       32
    .apk mmap      210        0       36        0
    .ttf mmap      131        0       56        0
    .dex mmap     3745        4     3532        0
    .oat mmap      281        0        4        0
    .art mmap     1376     1056        4        2
   Other mmap        5        4        0        0
    GL mtrack     4244     4244        0        0
      Unknown      822      812        0        0
        TOTAL    23990    17688     3644       70    60955    50794    10160

 App Summary
                       Pss(KB)
                        ------
           Java Heap:     2996
         Native Heap:     8648
                Code:     3844
               Stack:       40
            Graphics:     4244
       Private Other:     1560
              System:     2658

               TOTAL:    23990       TOTAL SWAP PSS:       70

 Objects
               Views:       47         ViewRootImpl:        2
         AppContexts:        6           Activities:        3
              Assets:        7        AssetManagers:        0
       Local Binders:       10        Proxy Binders:       32
       Parcel memory:        3         Parcel count:       14
    Death Recipients:        0      OpenSSL Sockets:        0
            WebViews:        0

 SQL
         MEMORY_USED:        0
  PAGECACHE_OVERFLOW:        0          MALLOC_SIZE:        0

* maps文件：cat /proc/29103/maps
    */

    //2.线上获取内存数据
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