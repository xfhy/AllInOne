package com.xfhy.allinone.actual.syncbarrier

import android.os.Build
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/10/19 22:56
 * Description : WatchSyncBarrier泄露
 */
@RequiresApi(Build.VERSION_CODES.M)
class WatchSyncBarrierThread : Thread() {

    private var barrierCount = 0
    private val messageQueue by lazy {
        Looper.getMainLooper().queue
    }
    //mMessages是禁止反射的，需配合 FreeReflection
    private val mMessagesField by lazy {
        val result = messageQueue.javaClass.getDeclaredField("mMessages")
        result.isAccessible = true
        result
    }

    override fun run() {
        while (!isInterrupted) {
            val messageHead = mMessagesField.get(messageQueue) as? Message
            messageHead?.let { message ->
                log("$message token=${message.arg1}")
                //该消息为同步屏障 && 该消息3秒没得到执行，先怀疑该同步屏障发生了泄露
                if (message.target == null && message.`when` - SystemClock.uptimeMillis() < -3000) {
                    //查看MessageQueue#postSyncBarrier(long when)源码得知，同步屏障message的arg1会携带token，
                    // 该token类似于同步屏障的序号，每个同步屏障的token是不同的，可以根据该token唯一标识一个同步屏障
                    val token = message.arg1
                    startCheckLeaking(token)
                }
            }
            sleep(2000)
        }
    }

    private fun startCheckLeaking(token: Int) {
        var checkCount = 0
        while (checkCount < 5) {
            //1. 判断该token对应的同步屏障是否还存在
            //2. 不存在就退出循环
            if(isSyncBarrierNotExist(token)) {
                break
            }
            //3. 存在的话，发1条异步消息给主线程Handler,再发1条同步消息给主线程Handler，
            // 看一下同步消息是否得到了处理，如果同步消息发了几次都没处理，而异步消息则发了几次都被处理了，说明SyncBarrier泄露了
            //4. 如果有泄露，那么就移除该泄露了的同步屏障(反射调用MessageQueue的removeSyncBarrier(int token))
            checkCount++
            SystemClock.sleep(1000)
        }
    }

    private fun isSyncBarrierNotExist(token: Int): Boolean {
        val messageHead = mMessagesField.get(messageQueue) as? Message
        messageHead?.let {
            //todo xfhy 未完待续
        }
        return true
    }

}