package com.xfhy.allinone.actual.idlehandler

import android.os.*
import android.os.MessageQueue.IdleHandler
import android.view.View
import androidx.annotation.RequiresApi
import com.xfhy.allinone.R
import com.xfhy.allinone.util.getMainThreadStackTrace
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/10/13
 * Description : 监控IdleHandler耗时问题
 * 把MessageQueue这个idlelist替换成自己的，重写add方法，添加进来的IdleHandler 给它包装一下，包装的那个类在执行queueIdle 时进行计时，这样添加进来的每个IdleHandler在执行的时候我们都能拿到其queueIdle 的执行时间。
 */
class WatchIdleHandlerActivity : TitleBarActivity() {

    override fun getThisTitle() = "监控IdleHandler耗时问题"

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_idle_handler)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startDetection(view: View) {
        val messageQueue = mHandler.looper.queue
        val messageQueueJavaClass = messageQueue.javaClass
        val mIdleHandlersField = messageQueueJavaClass.getDeclaredField("mIdleHandlers")
        mIdleHandlersField.isAccessible = true

        //得处理一下 系统不让反射mIdleHandlers  Method threw 'java.lang.IllegalAccessException' exception.
        //@UnsupportedAppUsage
        //https://github.com/tiann/FreeReflection
        val mIdleHandlers = mIdleHandlersField.get(messageQueue)
        mIdleHandlersField.set(messageQueue, MyArrayList())
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun startTimeConsuming(view: View) {
        mHandler.looper.queue.addIdleHandler {
            Thread.sleep(20000)
            false
        }
    }

}

class MyIdleHandler(private val originIdleHandler: IdleHandler, private val threadHandler: Handler) : IdleHandler {

    override fun queueIdle(): Boolean {
        log("开始执行idleHandler")

        //1. 延迟发送Runnable，Runnable收集主线程堆栈信息
        val runnable = {
            log("idleHandler卡顿 \n ${getMainThreadStackTrace()}")
        }
        threadHandler.postDelayed(runnable, 2000)
        val result = originIdleHandler.queueIdle()
        //2. idleHandler如果及时完成，那么就移除Runnable。如果上面的Runnable得到执行，说明主线程的idleHandler已经执行了2秒还没执行完，可以收集信息，对照着检查一下代码了
        threadHandler.removeCallbacks(runnable)
        return result
    }

    /*
    demo中获取到的日志信息：
    2022-10-17 07:33:50.282 28825-28825/com.xfhy.allinone D/xfhy_tag: 开始执行idleHandler
    2022-10-17 07:33:52.286 28825-29203/com.xfhy.allinone D/xfhy_tag: idleHandler卡顿
     java.lang.Thread.sleep(Native Method)
    java.lang.Thread.sleep(Thread.java:443)
    java.lang.Thread.sleep(Thread.java:359)
    com.xfhy.allinone.actual.idlehandler.WatchIdleHandlerActivity$startTimeConsuming$1.queueIdle(WatchIdleHandlerActivity.kt:47)
    com.xfhy.allinone.actual.idlehandler.MyIdleHandler.queueIdle(WatchIdleHandlerActivity.kt:62)
    android.os.MessageQueue.next(MessageQueue.java:465)
    android.os.Looper.loop(Looper.java:176)
    android.app.ActivityThread.main(ActivityThread.java:8668)
    java.lang.reflect.Method.invoke(Native Method)
    com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
    com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)
    * */

}

class MyArrayList : ArrayList<IdleHandler>() {

    private val handlerThread by lazy {
        HandlerThread("").apply {
            start()
        }
    }
    private val threadHandler by lazy {
        Handler(handlerThread.looper)
    }

    override fun add(element: IdleHandler): Boolean {
        return super.add(MyIdleHandler(element, threadHandler))
    }

}
