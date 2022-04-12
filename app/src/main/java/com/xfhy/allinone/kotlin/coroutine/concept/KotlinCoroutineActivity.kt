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

}