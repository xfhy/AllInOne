package com.xfhy.allinone.kotlin.coroutine.concept

import com.xfhy.library.ext.log
import kotlinx.coroutines.*

/**
 * @author : xfhy
 * Create time : 2022/4/8 8:06 上午
 * Description : 协程取消相关
 */
class CoroutineCancel {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    fun testNpe() {
        scope.launch {

            val job1 = launch {
                log("job1")
                throw NullPointerException()
            }
            val job2 = launch {
                log("job2")
            }

            // 取消协程
            //jobScope.cancel()
            //job1.cancel()
        }
    }

    fun testCancel() {
        scope.launch {
            val job1 = launch {
                delay(1000)
                log("job1")
            }
            job1.cancel()
        }
    }

    fun testCancelEarly() {
        val startTime = System.currentTimeMillis()

        scope.launch {
            val job = scope.launch {
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

}