package com.xfhy.allinone.performance.memory

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.xfhy.allinone.databinding.ActivityMemoryLeakBinding
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlin.concurrent.thread

/**
 * @author : xfhy
 * Create time : 2021/4/21 6:40 AM
 * Description : 内存泄露
 */
class MemoryLeakActivity : TitleBarActivity() {

    private lateinit var memoryLeakBinding: ActivityMemoryLeakBinding
    override fun getThisTitle() = "内存泄露"
    private val myHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            log("消息来了")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memoryLeakBinding = ActivityMemoryLeakBinding.inflate(layoutInflater)
        setContentView(memoryLeakBinding.root)

        //Handler内存泄露的原因：非静态内部类生命周期比外部类生命周期长。非静态内部类持有了外部类的引用，
        // 也就是Handler持有了Activity的引用,而这个Handler没有即时得到回收，引用链如下：
        // `主线程 —> threadlocal —> Looper —> MessageQueue —> Message —> Handler —> Activity`，于是发生内存泄露。
        //延时20s
        memoryLeakBinding.btnSendDelayMsg.setOnClickListener {
            myHandler.postDelayed({
                log("dadasd")
            }, 20000)
        }

        memoryLeakBinding.btnFinish.setOnClickListener {
            finish()
        }

        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Exception) {
                    log("捕获异常 ${e.message}")
                }
            }
        }

        memoryLeakBinding.btnCrash.setOnClickListener {
            //val a = 3/0
            thread {
                val a=3/0
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
    }

}