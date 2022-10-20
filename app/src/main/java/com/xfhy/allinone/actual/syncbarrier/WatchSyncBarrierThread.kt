package com.xfhy.allinone.actual.syncbarrier

import android.annotation.SuppressLint
import android.os.*
import androidx.annotation.RequiresApi
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/10/19 22:56
 * Description : WatchSyncBarrier泄露
 *
 * 1. 开个子线程，轮询检查主线程的MessageQueue里面的message，检查是否有同步屏障消息的when已经过去了很久了，但还没得到执行
 * 2. 此时可以合理怀疑该同步屏障消息可能已泄露，但还不能确定
 * 3. 这个时候，往主线程发一个同步消息和一个异步消息（可以间隔地多发几次，增加可信度），如果同步消息没有得到执行，但异步消息得到执行了，这说明什么？
 * 说明主线程的MessageQueue中有一个同步屏障一直没得到移除，所以同步消息才没得到执行，而异步消息得到执行了。
 * 4. 此时，可以激进一点，把这个泄露的同步泄露消息给移除掉。
 *
 */
@SuppressLint("DiscouragedPrivateApi")
@RequiresApi(Build.VERSION_CODES.M)
class WatchSyncBarrierThread : Thread() {

    private var barrierCount = 0

    private val mainThreadMessageQueue by lazy {
        Looper.getMainLooper().queue
    }

    //mMessages是禁止反射的，需配合 FreeReflection
    private val mMessagesField by lazy {
        val result = mainThreadMessageQueue.javaClass.getDeclaredField("mMessages")
        result.isAccessible = true
        result
    }
    private val messageNextField by lazy {
        val declaredField = Message::class.java.getDeclaredField("next")
        declaredField.isAccessible = true
        declaredField
    }

    override fun run() {
        while (!isInterrupted) {
            val messageHead = mMessagesField.get(mainThreadMessageQueue) as? Message
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
        barrierCount = 0
        while (checkCount < 5) {
            checkCount++
            //1. 判断该token对应的同步屏障是否还存在,不存在就退出循环
            if (isSyncBarrierNotExist(token)) {
                break
            }
            //2. 存在的话，发1条异步消息给主线程Handler,再发1条同步消息给主线程Handler，
            // 看一下同步消息是否得到了处理，如果同步消息发了几次都没处理，而异步消息则发了几次都被处理了，说明SyncBarrier泄露了
            if (detectSyncBarrierOnce()) {
                //发生了SyncBarrier泄露
                //3. 如果有泄露，那么就移除该泄露了的同步屏障(反射调用MessageQueue的removeSyncBarrier(int token))
                removeSyncBarrier(token)
                break
            }
            SystemClock.sleep(1000)
        }
    }

    private fun removeSyncBarrier(token: Int) {
        //直接反射调用removeSyncBarrier方法移除泄露的同步屏障消息
        val removeSyncBarrierMethod = mainThreadMessageQueue.javaClass.getDeclaredMethod("removeSyncBarrier",Int::class.java)
        removeSyncBarrierMethod.isAccessible = true
        removeSyncBarrierMethod.invoke(mainThreadMessageQueue, token)
    }

    private fun detectSyncBarrierOnce(): Boolean {
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.arg1) {
                    -1 -> {
                        //异步消息
                        barrierCount++
                    }
                    0 -> {
                        //同步消息 说明主线程的同步消息是能做事的啊，就没有SyncBarrier一说了
                        barrierCount = 0
                    }
                    else -> {}
                }
            }
        }

        val asyncMessage = Message.obtain()
        asyncMessage.isAsynchronous = true
        asyncMessage.arg1 = -1

        val syncMessage = Message.obtain()
        syncMessage.arg1 = 0

        handler.sendMessage(asyncMessage)
        handler.sendMessage(syncMessage)

        //超过3次，主线程的同步消息还没被处理，而异步消息缺得到了处理，说明确实是发生了SyncBarrier泄露
        return barrierCount > 3
    }

    /**
     * 判断arg1为token的同步屏障是否存在，不存在则返回true
     */
    private fun isSyncBarrierNotExist(token: Int): Boolean {
        val messageHead = mMessagesField.get(mainThreadMessageQueue) as? Message
        messageHead?.let {
            var p = messageHead
            while (p != null) {
                if (p.arg1 == token && p.target == null) {
                    return false
                }
                p = messageNextField.get(p) as? Message
            }
        }
        return true
    }

}