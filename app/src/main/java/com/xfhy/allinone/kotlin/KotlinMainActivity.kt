package com.xfhy.allinone.kotlin

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.kotlin.coroutine.concept.KotlinCoroutineActivity
import com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowActivity
import com.xfhy.allinone.kotlin.coroutine.jetpack.JetpackCoroutineActivity
import com.xfhy.allinone.kotlin.coroutine.retrofit.RetrofitWithCoroutineActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_kotlin_main.*
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/6/13 6:41 上午
 * Description : Kotlin首页
 */
class KotlinMainActivity : TitleBarActivity() {
    override fun getThisTitle() = "Kotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_main)
        initView()
    }

    private fun initView() {
        btnGoJetpackCoroutine.setOnClickListener {
            startActivity<JetpackCoroutineActivity>()
        }
        btnGoCancelAndException.setOnClickListener {
            startActivity<KotlinCoroutineActivity>()
        }
        btnGoKotlinCoroutineWithRetrofit.setOnClickListener {
            startActivity<RetrofitWithCoroutineActivity>()
        }
        btnGoKotlinFlow.setOnClickListener {
            startActivity<KotlinFlowActivity>()
        }
    }

}