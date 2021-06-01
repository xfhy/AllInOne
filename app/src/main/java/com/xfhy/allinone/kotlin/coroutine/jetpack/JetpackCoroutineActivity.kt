package com.xfhy.allinone.kotlin.coroutine.jetpack

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_kotlin_jetpack_coroutine.*

/**
 * @author : xfhy
 * Create time : 2021/5/30 6:41 上午
 * Description :
 */
class JetpackCoroutineActivity : TitleBarActivity() {

    override fun getThisTitle() = "Kotlin 协程与架构组件一起使用"
    private val jetpackCoroutineViewModel by lazy { ViewModelProvider.NewInstanceFactory().create(JetpackCoroutineViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_jetpack_coroutine)

        initView()
        initData()

        /* lifecycleScope.launch {
             repeat(100000) {
                 delay(100)
                 tvText.text = "$it"
                 log("$it")
             }
         }

         lifecycleScope.launchWhenCreated {
             repeat(100000) {
                 delay(100)
                 tvText.text = "$it"
                 log("$it")
             }
         }
         lifecycleScope.launch {
             whenCreated {
                 repeat(100000) {
                     delay(100)
                     tvText.text = "$it"
                     log("$it")
                 }
             }
         }*/
        lifecycleScope.launchWhenResumed {
            //这里面的代码执行执行一次
            log("卧槽  launchWhenResumed")
        }


    }

    private fun initView() {

    }

    private fun initData() {
        btnGetDataByViewModelScope.setOnClickListener {
            jetpackCoroutineViewModel.launchData()
        }

        jetpackCoroutineViewModel.netData.observe(this) {
            tvText.text = it
        }
    }

    override fun onResume() {
        super.onResume()
        log("卧槽  onResume")
    }

}