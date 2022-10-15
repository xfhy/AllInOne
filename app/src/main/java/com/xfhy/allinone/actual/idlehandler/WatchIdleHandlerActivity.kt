package com.xfhy.allinone.actual.idlehandler

import android.os.*
import android.os.MessageQueue.IdleHandler
import android.view.View
import androidx.annotation.RequiresApi
import com.xfhy.allinone.R
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

class MyIdleHandler(private val originIdleHandler: IdleHandler) : IdleHandler {

    override fun queueIdle(): Boolean {
        val startTime = System.currentTimeMillis()
        log("开始执行idleHandler")
        val result = originIdleHandler.queueIdle()
        if (System.currentTimeMillis() - startTime > 2000) {
            // todo xfhy : do something
            log("idleHandler卡顿")
        }
        return result
    }
}

class MyArrayList : ArrayList<IdleHandler>() {

    override fun add(element: IdleHandler): Boolean {
        return super.add(MyIdleHandler(element))
    }

}
