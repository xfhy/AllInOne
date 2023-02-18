package com.xfhy.nativelib

import android.content.Context
import com.bytedance.raphael.Raphael




/**
 * @author : xfhy
 * Create time : 2023/2/18 9:45
 * Description :  字节的 native 内存泄漏监控工具  memory-leak-detector
 */
object BMemoryLeakDetector {

    /**
     * 开始监测（需要有存储权限）
     */
    fun startDetector(context :Context) {
        // 监控指定的so
//        Raphael.start(
//            Raphael.MAP64_MODE or Raphael.ALLOC_MODE or 0x0F0000 or 1024,
//            context.getExternalFilesDir("")?.absolutePath,
//            ".*libtestmalloc\\.so$"
//        )

        // 监控整个进程
        Raphael.start(
            Raphael.MAP64_MODE or Raphael.ALLOC_MODE or 0x0F0000 or 1024,
            context.getExternalFilesDir("")?.absolutePath,
            null
        )


        //## 通过本地广播监控指定的so
        //## 0x0CF0400=Raphael.MAP64_MODE|Raphael.ALLOC_MODE|0x0F0000|1024
        //adb shell am broadcast -a com.bytedance.raphael.ACTION_START -f 0x01000000 --es configs 0xCF0400 --es regex ".*libXXX\\.so$"
        //## 监控整个进程（RaphaelReceiver 组件所在的进程）
        //## 0x0CF0400=Raphael.MAP64_MODE|Raphael.ALLOC_MODE|0x0F0000|1024
        //adb shell am broadcast -a com.bytedance.raphael.ACTION_START -f 0x01000000 --es configs 0xCF0400
    }

    /**
     * 输出结果  结果在存储卡的 context.getExternalFilesDir("")?.absolutePath->/storage/emulated/0/Android/data/com.xfhy.allinone/files 里面
     */
    fun printResult() {
        // 代码控制
        Raphael.print()

        //## 本地广播
        //adb shell am broadcast -a com.bytedance.raphael.ACTION_PRINT -f 0x01000000
    }

    //开始分析
    //## 聚合 report，该文件在 print/stop 之后生成，需要手动 pull 出来
    //## 用到离线符号符号化功能的，需将raphael.py里的addr2line改为自己本地的NDK路径
    //##   -r: 日志路径, 必需，手机端生成的report文件
    //##   -o: 输出文件名，非必需，默认为 leak-doubts.txt
    //##   -s: 符号表目录，非必需，有符号化需求时可传，符号表文件需跟so同名，如：libXXX.so，多个文件需放在同一目录下
    //raphael.py和mmap.py文件：https://github.com/bytedance/memory-leak-detector/tree/master/library/src/main/python
    //python3 library/src/main/python/raphael.py -r report -o leak-doubts.txt -s ./symbol/
    //
    //## 数据格式说明
    //##  201,852,591	totals // 单指raphael拦截到的未释放的虚拟内存总和
    //##  118,212,424	libandroid_runtime.so
    //##   28,822,002	libhwui.so
    //##   24,145,920	libstagefright.so
    //##   15,679,488	libv8.cr.so
    //##    9,566,192	libc++_shared.so
    //##       25,536	libsqlite.so
    //##       12,288	libv8_libbase.cr.so
    //##    5,388,741	extras // raphael.py里预设了一些通用配置，可以通过修改规则进一步识别分组到extras里的数据
    //##
    //##
    //## bdb11000, 70828032, 66 => bdb11000是report里此堆栈第一次分配出的内存地址，70828032是report里此堆栈的内存总和，66是report里此堆栈的总次数
    //## 0x000656cf /system/lib/libc.so (pthread_create + 246)
    //## 0x0037c129 /system/lib/libart.so (art::Thread::CreateNativeThread(_JNIEnv*, _jobject*, unsigned int, bool) + 448)
    //## 0x00112137 /system/framework/arm/boot.oat (java.lang.Thread.nativeCreate + 142)

    //我获取到的数据
    //  370,036,240	totals
    //  369,098,752	libtestmalloc.so
    //      181,912	libhwui.so
    //       61,504	libbytehook.so
    //       26,424	libandroid_runtime.so
    //      667,648	extras
    //
    //0x0000007dafe00000, 276824064, 3
    //0x0000000000000790 /data/app/com.xfhy.allinone-J_kERSV9Oj7Wbe7yDM3qpg==/lib/arm64/libtestmalloc.so (Java_com_xfhy_nativelib_TestMalloc_testMalloc + 28)
    //0x0000000000577fe4 /system/lib64/libart.so (art_quick_generic_jni_trampoline + 148)
    //
    //0x0000007da9e00000, 92274688, 1
    //0x0000000000000720 /data/app/com.xfhy.allinone-J_kERSV9Oj7Wbe7yDM3qpg==/lib/arm64/libtestmalloc.so (testMallocMethod(_JNIEnv*) + 36)
    //0x0000000000000790 /data/app/com.xfhy.allinone-J_kERSV9Oj7Wbe7yDM3qpg==/lib/arm64/libtestmalloc.so (Java_com_xfhy_nativelib_TestMalloc_testMalloc + 28)
    //0x0000000000577fe4 /system/lib64/libart.so (art_quick_generic_jni_trampoline + 148)

    //分析maps文件
    //## 分析 maps
    //##  -m: maps文件路径，必需
    //python3 library/src/main/python/mmap.py -m maps


    /**
     * 停止监测
     */
    fun stopMonitor() {
        // 代码控制
        Raphael.stop()


        //## 广播控制
        //adb shell am broadcast -a com.bytedance.raphael.ACTION_STOP -f 0x01000000
    }

}