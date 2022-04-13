package com.xfhy.allinone.kotlin.coroutine.concept

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_kotlin_coroutine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

}