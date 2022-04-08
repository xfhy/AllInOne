package com.xfhy.allinone.kotlin.coroutine.concept

import com.xfhy.library.ext.log
import kotlinx.coroutines.*

/**
 * @author : xfhy
 * Create time : 2022/4/8 8:06 上午
 * Description : 协程取消相关
 */
class CoroutineCancel {

    private val jobScope = CoroutineScope(Job() + Dispatchers.Default)

    fun testCancel(){
        jobScope.launch {

            val job1 = launch {
                delay(1000)
                log("job1")
            }
            val job2 = launch {
                delay(1000)
                log("job2")
            }

            // 取消协程
            jobScope.cancel()
            //job1.cancel()
        }
    }

}