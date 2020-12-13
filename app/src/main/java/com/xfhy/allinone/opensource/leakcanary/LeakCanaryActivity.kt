package com.xfhy.allinone.opensource.leakcanary

import android.content.Context
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * LeakCanary解析
 *
 * 1. 在AppWatcherInstaller这个ContentProvider中进行的初始化,所以不需要我们手动去Application里面初始化
 *
 *  博客输出: https://xfhy666.blog.csdn.net/article/details/111072036
 *
 *  实现要点:
 *
 *   1. 当一个对象需要被回收时,生成一个唯一的key,将它们封装进KeyedWeakReference中,并传入自定义的ReferenceQueue
 *   2. 将key和KeyedWeakReference放入一个map中
 *   3. 过一会儿之后(默认是5秒)主动触发GC,将自定义的ReferenceQueue中的KeyedWeakReference全部移除(它们所引用的对象已被回收),并同时根据这些KeyedWeakReference的key将map中的KeyedWeakReference也移除掉.
 *   4. 此时如果map中还有KeyedWeakReference剩余,那么就是没有入队的,也就是说这些KeyedWeakReference所对应的对象还没被回收.这是不合理的,这里就产生了内存泄露.
 *   5. 将这些内存泄露的对象分析引用链,保存数据
 *
 */
class LeakCanaryActivity : TitleBarActivity() {

    companion object {
        //强行泄露一下Activity实例  方便测试
        var mTestContext: Context? = null
    }

    override fun getThisTitle(): CharSequence {
        return "LeakCanary"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak_canary)

        mTestContext = this
    }

    data class Fruit(val name: String)

}


fun main() {

    val referenceQueue = ReferenceQueue<LeakCanaryActivity.Fruit>()
    var apple: LeakCanaryActivity.Fruit? = LeakCanaryActivity.Fruit("苹果")
    val weakReference = WeakReference<LeakCanaryActivity.Fruit>(apple, referenceQueue)

    apple = null

    System.gc()
    try {
        Thread.sleep(5000)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val poll = referenceQueue.poll()
    //log("xfhy_leak", "apple对象被回收了,队列里面的是weakReference这个对象 $poll")
    println("apple对象被回收了,队列里面的是weakReference这个对象 $poll")
}
