package com.xfhy.allinone.actual.syncbarrier

import android.os.*
import android.os.MessageQueue.IdleHandler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.allinone.util.getMainThreadStackTrace
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author : xfhy
 * Create time : 2022/10/19
 * Description : 监控SyncBarrier泄露问题
 *
 * 背景：
 * 1.线程不安全，post了多个同步屏障
2.post了同步屏障之后，把异步消息给移除了，导致没有移除该同步屏障

解决：开个子线程，轮询监控主线程的messageQueue，看里面是否存在同步屏障，存在的话再看一下它的when，是否已存在已久。
如果是存在已久，说明可能存在问题，当然也可能是主线程当前有点卡顿，当时同步屏幕还没被移除。可以先往泄露方向怀疑，
这个时候发个异步消息，再发个同步消息到主线程，如果异步消息被处理，同步消息未被处理，那么就是泄露了，这个时候把这个同步屏障给移除掉。
 *
 */
class WatchSyncBarrierActivity : TitleBarActivity() {

    override fun getThisTitle() = "监控SyncBarrier泄露问题"

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val watchSyncBarrierThread by lazy {
        WatchSyncBarrierThread()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_sync_barrier)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun makeSyncBarrierLeak(view: View) {
        //制造SyncBarrier泄露
        val messageQueue = mainHandler.looper.queue
        kotlin.runCatching {
            //postSyncBarrier是禁止反射的，需配合 FreeReflection
            val postSyncBarrierMethod = messageQueue::class.java.getDeclaredMethod("postSyncBarrier")
            postSyncBarrierMethod.isAccessible = true
            postSyncBarrierMethod.invoke(messageQueue)
            log("执行成功")
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun startTestAnimation(view: View) {
        //执行一个动画
        view.animate().scaleX(2f).scaleY(2f).start()
    }

    fun startDetection(view: View) {
        //开始检测SyncBarrier泄露
        watchSyncBarrierThread.start()
    }

}