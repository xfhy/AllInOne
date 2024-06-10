package com.xfhy.allinone.kotlin.coroutine.flow

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_kotlin_flow.btn_get_wx_data
import kotlinx.android.synthetic.main.activity_kotlin_flow.tv_data
import kotlinx.coroutines.launch

/**
 * @author : guoliang08
 * Create time : 2024年05月29日07:32:06
 * Description : Kotlin Flow
 */
class KotlinFlowActivity : TitleBarActivity() {

    private val flowViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(
            KotlinFlowViewModel::class.java
        )
    }

    override fun getThisTitle(): CharSequence {
        return "Kotlin Flow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_flow)
        initView()
    }

    private fun initView() {
        btn_get_wx_data.setOnClickListener {
            lifecycleScope.launch {
                flowViewModel.getWxData().collect { newData ->
                    tv_data.text = newData?.data?.getOrNull(0)?.name ?: "没获取到数据"
                }
                flowViewModel.getWxData().collect { newData ->
                    Log.d("xfhy666", "${newData?.data?.getOrNull(0)?.name ?: "没获取到数据"}")
                }
            }
        }
    }

}