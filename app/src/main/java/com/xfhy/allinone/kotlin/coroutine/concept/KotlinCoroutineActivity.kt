package com.xfhy.allinone.kotlin.coroutine.concept

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_kotlin_coroutine.*
import kotlinx.coroutines.*

/**
 * @author : xfhy
 * Create time : 2022年04月06日07:40:03
 * Description : Kotlin协程  取消和异常
 */
class KotlinCoroutineActivity : TitleBarActivity() {
    private val coroutineCancel = CoroutineCancel()

    override fun getThisTitle() = "Kotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_coroutine)
        initView()
        launchDemo()
    }

    private fun launchDemo() {
        /* val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
         scope.launch {
             //新的协程,它的parent是CoroutineScope
             val result = async {
                 //新的协程  它的parent是launch
             }.await()
         }*/
//        val coroutineCancel = CoroutineCancel()
//        coroutineCancel.testCancel()
    }

    private fun initView() {
        btnCancel.setOnClickListener {
            coroutineCancel.testCancel()
        }
    }

    fun testCancelEarly(view: View) {
        //coroutineCancel.testCancelEarly()
        val startTime = System.currentTimeMillis()
        lifecycleScope.launch {
            val job = launch(Dispatchers.IO) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("Hello ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1000L)
            log("Cancel")
            job.cancel()
            log("Done")
        }
    }

    //取消不了
    fun testCancellationIsNotPossible(view: View) {
        val startTime = System.currentTimeMillis()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 15) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("Hello ${i++}")
                        nextPrintTime += 500L
                    }
                    if (i == 5) {
                        finish()
                    }
                }
            }

            withContext(Dispatchers.Default) {
                log("Hello Dispatchers.Default")
            }

            log("Done")
        }
        //因为在5的时候,进行了finish,所以onDestroy被打印出来了. 这个时候其实lifecycleScope已经cancel了,然而withContext并没有结束,因为它没有感知到已经cancel了,继续执行,一直到执行完成.
        //但最后的Done没有打印出来,因为已经cancel了,不会再切线程回来执行了.
        //打印结果:
        //2022-04-13 08:11:25.494 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 0
        //2022-04-13 08:11:25.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 1
        //2022-04-13 08:11:26.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 2
        //2022-04-13 08:11:26.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 3
        //2022-04-13 08:11:27.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 4
        //2022-04-13 08:11:27.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 5
        //2022-04-13 08:11:27.999 15739-15739/com.xfhy.allinone D/xfhy_tag: onDestroy
        //2022-04-13 08:11:28.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 6
        //2022-04-13 08:11:28.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 7
        //2022-04-13 08:11:29.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 8
        //2022-04-13 08:11:29.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 9
        //2022-04-13 08:11:30.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 10
        //2022-04-13 08:11:30.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 11
        //2022-04-13 08:11:31.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 12
        //2022-04-13 08:11:31.931 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 13
        //2022-04-13 08:11:32.431 15739-15794/com.xfhy.allinone D/xfhy_tag: Hello 14
    }

    fun cancellableWithContext(view: View) {
        val startTime = System.currentTimeMillis()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 15 /* && isActive*/) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("Hello ${i++}")
                        nextPrintTime += 1000L
                    }
                    if (i == 5) {
                        finish()
                    }
                    ensureActive()
                }
            }
            log("Done")
        }
        //打印结果:
        //2022-04-14 07:47:28.981 17402-17455/com.xfhy.allinone D/xfhy_tag: Hello 0
        //2022-04-14 07:47:29.977 17402-17455/com.xfhy.allinone D/xfhy_tag: Hello 1
        //2022-04-14 07:47:30.977 17402-17455/com.xfhy.allinone D/xfhy_tag: Hello 2
        //2022-04-14 07:47:31.977 17402-17455/com.xfhy.allinone D/xfhy_tag: Hello 3
        //2022-04-14 07:47:32.977 17402-17455/com.xfhy.allinone D/xfhy_tag: Hello 4
        //2022-04-14 07:47:33.547 17402-17402/com.xfhy.allinone D/xfhy_tag: onDestroy
    }

    fun cancellableDelay(view: View) {
        lifecycleScope.launch {
            delay(2000)
            log("Hello")
            finish()
            log("finish")
            delay(3000)
            log("World")
        }
        //打印结果:
        //2022-04-14 08:01:22.133 18324-18324/com.xfhy.allinone D/xfhy_tag: Hello
        //2022-04-14 08:01:22.137 18324-18324/com.xfhy.allinone D/xfhy_tag: finish
        //2022-04-14 08:01:22.719 18324-18324/com.xfhy.allinone D/xfhy_tag: onDestroy
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    fun yieldTest(view: View) {
        val singleDispatcher = newSingleThreadContext("singleDispatcher")
        lifecycleScope.launch(singleDispatcher) {
            launch {
                withContext(singleDispatcher) {
                    repeat(3) {
                        printSomeThingBlock("Task1 $it")
                        yield()
                    }
                }
            }
            launch {
                withContext(singleDispatcher) {
                    repeat(3) {
                        printSomeThingBlock("Task2 $it")
                        yield()
                    }
                }
            }
        }
        //没有加yield()的情况下，打印结果：
        //2022-04-19 07:04:51.570 13079-13141/com.xfhy.allinone D/xfhy_tag: Task1 0
        //2022-04-19 07:04:51.570 13079-13141/com.xfhy.allinone D/xfhy_tag: Task1 1
        //2022-04-19 07:04:51.570 13079-13141/com.xfhy.allinone D/xfhy_tag: Task1 2
        //2022-04-19 07:04:51.571 13079-13141/com.xfhy.allinone D/xfhy_tag: Task2 0
        //2022-04-19 07:04:51.571 13079-13141/com.xfhy.allinone D/xfhy_tag: Task2 1
        //2022-04-19 07:04:51.571 13079-13141/com.xfhy.allinone D/xfhy_tag: Task2 2

        //加了yield()的情况下，打印结果：
        //2022-04-19 07:05:41.633 13360-13406/com.xfhy.allinone D/xfhy_tag: Task1 0
        //2022-04-19 07:05:41.633 13360-13406/com.xfhy.allinone D/xfhy_tag: Task2 0
        //2022-04-19 07:05:41.634 13360-13406/com.xfhy.allinone D/xfhy_tag: Task1 1
        //2022-04-19 07:05:41.634 13360-13406/com.xfhy.allinone D/xfhy_tag: Task2 1
        //2022-04-19 07:05:41.635 13360-13406/com.xfhy.allinone D/xfhy_tag: Task1 2
        //2022-04-19 07:05:41.636 13360-13406/com.xfhy.allinone D/xfhy_tag: Task2 2
    }

    private fun printSomeThingBlock(s: String) {
        log(s)
    }

    //fun testJobCancel(view: View) {
    //    val startTime = System.currentTimeMillis()
    //    lifecycleScope.launch {
    //        val job = launch {
    //            var i = 0
    //            var nextPrintTime = startTime
    //            while (i < 5) {
    //                if (System.currentTimeMillis() >= nextPrintTime) {
    //                    log("Hello ${i++}")
    //                    nextPrintTime += 1000L
    //                }
    //            }
    //        }
    //        log("job isActive: ${job.isActive}")
    //        log("cancel job")
    //        job.cancel()
    //        log("job isActive: ${job.isActive}")
    //        log("join job")
    //        job.join()
    //    }
    //    //打印结果：
    //    //job isActive: true
    //    //cancel job
    //    //job isActive: false
    //    //join job
    //}

    fun testJobCancel(view: View) {
        val startTime = System.currentTimeMillis()
        lifecycleScope.launch {
            val job = launch {
                var i = 0
                var nextPrintTime = startTime
                while (i < 5 && isActive) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("Hello ${i++}")
                        nextPrintTime += 1000L
                    }
                    cancel()
                    log("inner isActive: ${isActive}")
                }
            }
            log("job isActive: ${job.isActive}")
            log("join job")
            job.join()
        }
        //打印结果：
        //job isActive: true
        //join job
        //Hello 0
        //inner isActive: false
    }

    fun cleanByTryCatch(view: View) {
        suspend fun work() {
            val startTime = System.currentTimeMillis()
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                yield()
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    log("Hello ${i++}")
                    nextPrintTime += 500L
                }
            }
        }

        lifecycleScope.launch {
            val job = launch(Dispatchers.Default) {
                try {
                    work()
                } finally {
                    withContext(NonCancellable) {
                        delay(2000L)
                        log("Cleanup done!")
                    }
                }
            }
            delay(1000L)
            log("Cancel!")
            job.cancel()
            log("Done!")
        }
        //打印结果：
        //Hello 0
        //Hello 1
        //Hello 2
        //Cancel!
        //Done!
        //Cleanup done!
    }

    fun cleanByInvokeOnCancellation(view: View) {
        suspend fun work() {
            return suspendCancellableCoroutine { continuation ->
                continuation.invokeOnCancellation {
                    // do cleanup
                    log("Cleanup done!")
                }
                // rest of the implementation
                val startTime = System.currentTimeMillis()
                var nextPrintTime = startTime
                var i = 0

                while (i < 5 && continuation.isActive) {
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("Hello ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
        }
        lifecycleScope.launch {
            val job = launch(Dispatchers.Default) {
                work()
            }
            delay(1000L)
            log("Cancel!")
            job.cancel()
            log("Done!")
        }
        //打印结果:
        //Hello 0
        //Hello 1
        //Hello 2
        //Cancel!
        //Cleanup done!
        //Done!
    }


}