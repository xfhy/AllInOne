package com.xfhy.allinone.kotlin.coroutine.flow

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_kotlin_flow.*
import kotlinx.coroutines.launch

/**
 * @author : xfhy
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
        btn_insert_user_data.setOnClickListener {
            flowViewModel.insertUserData()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    flowViewModel.userDataList.collect { dataList ->
                        log("数据库中的数据总个数为 : ${dataList?.size}")
                    }
                }
            }
        }

        btn_get_wx_data.setOnClickListener {

//            lifecycleScope.launch {
//                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    // 单独放到launch去收集,否则如果是遇到StateFlow的collect的话,会阻塞在那里,后面的代码执行不到,到ui 的destroy时才执行得到
//                    launch {
//                        flowViewModel.getWxData().collect { newData ->
//                            Log.d("xfhy666", "getWxData ${newData?.data?.getOrNull(0)?.name}")
//                            tv_data.text = newData?.data?.getOrNull(0)?.name ?: "没获取到数据"
//                        }
//                    }
////                   launch {
////                       flowViewModel.getListData().collect { data ->
////                           Log.d("xfhy666", "getListData 获取到的数据 $data")
////                       }
////                   }
////
////                    launch {
////                        flowViewModel.getThrowFlow().collect {
////                            Log.d("xfhy666", "getThrowFlow 获取到的数据 $it")
////                        }
////                    }
//
////                    launch {
////                        flowViewModel.errorUseFlow1().collect() {
////                            Log.d("xfhy666", "errorUseFlow1 获取到的数据 $it")
////                        }
////                    }
//                }
//            }
        }
    }

}