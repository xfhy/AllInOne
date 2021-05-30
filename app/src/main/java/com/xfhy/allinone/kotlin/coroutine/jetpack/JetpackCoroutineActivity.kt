package com.xfhy.allinone.kotlin.coroutine.jetpack

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
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
    }

    private fun initView() {

    }

    private fun initData() {
        btnGetDataByViewModelScope.setOnClickListener {
            jetpackCoroutineViewModel.launchData()
        }
    }
}