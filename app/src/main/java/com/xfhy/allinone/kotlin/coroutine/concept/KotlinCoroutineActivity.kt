package com.xfhy.allinone.kotlin.coroutine.concept

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2022年04月06日07:40:03
 * Description : Kotlin协程  取消和异常
 */
class KotlinCoroutineActivity : TitleBarActivity() {
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
        val coroutineCancel = CoroutineCancel()
        coroutineCancel.testCancel()
    }

    private fun initView() {

    }

}