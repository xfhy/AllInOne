package com.xfhy.allinone.kotlin.coroutine.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xfhy.allinone.R
import kotlinx.android.synthetic.main.activity_kotlin_flow.*
import kotlinx.coroutines.launch

/**
 * @author : xfhy
 * Create time : 2024年05月29日07:32:06
 * Description : Kotlin Flow
 */
class KotlinFlowActivity : AppCompatActivity() {

    private val flowViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(
            KotlinFlowViewModel::class.java
        )
    }

//    override fun getThisTitle(): CharSequence {
//        return "Kotlin Flow"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_flow)
        initView()
        initData()
    }

    private fun initData() {
//        flowViewModel.fetchData1()
//        flowViewModel.livedata1.observe(this) {
//            log("livedata1 数据 $it")
//        }
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.flow1.collect {
//                    log("flow1 数据 $it")
//                }
//            }
//        }
//
//        flowViewModel.fetchData2()
//        flowViewModel.liveDataB.observe(this) {
//            log("liveDataB 数据 $it")
//        }
//        flowViewModel.fetchFlowA()
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.flowB.collect {
//                    log("flowB 数据 $it")
//                }
//            }
//        }

//        flowViewModel.fetchData3()
//        flowViewModel.mediatorLiveData.observe(this) {
//            log("mediatorLiveData 数据 $it")
//        }
//        flowViewModel.fetchData3ByFlow()
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.combinedFlow.collect {
//                    log("combinedFlow 数据 $it")
//                }
//            }
//        }

//        flowViewModel.mappedLiveData.observe(this) {
//            log("mappedLiveData data : $it")
//        }
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.mappedFlow.collect {
//                    log("flow4 data $it")
//                }
//            }
//        }

//        flowViewModel.switchMappedLiveData.observe(this) {
//            log("switchMappedLiveData data : $it")
//        }
//        flowViewModel.fetchData5()
//
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.switchMappedFlow.collect {
//                    log("switchMappedFlow data : $it")
//                }
//            }
//        }
//        flowViewModel.fetchFlow5()

//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flowViewModel.stateInFlow.collect {
//                    log("stateInFlow data $it")
//                }
//            }
//        }

        flowViewModel.testShareIn()
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                launch {
//                    flowViewModel.shareInFlow.collect {
//                        log("订阅者1 shareInFlow data $it")
//                    }
//                }
//                delay(400L)
//                launch {
//                    flowViewModel.shareInFlow.collect {
//                        log("订阅者2 shareInFlow data $it")
//                    }
//                }
//            }
//        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 单独放到launch去收集,否则如果是遇到StateFlow的collect的话,会阻塞在那里,后面的代码执行不到,到ui 的destroy时才执行得到
                launch {
                    flowViewModel.wxData.collect { newData ->
                        Log.d("xfhy666", "getWxData ${newData?.data?.getOrNull(0)?.name}")
                        tv_data.text = newData?.data?.getOrNull(0)?.name ?: "没获取到数据"
                    }
                }
            }
        }

    }

    private fun initView() {
        btn_insert_user_data.setOnClickListener {
            flowViewModel.insertUserData()
        }

//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                launch {
//                    flowViewModel.userDataList.collect { dataList ->
//                        log("数据库中的数据总个数为 : ${dataList?.size}")
//                    }
//                }
//            }
//        }

        btn_get_wx_data.setOnClickListener {

            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // 单独放到launch去收集,否则如果是遇到StateFlow的collect的话,会阻塞在那里,后面的代码执行不到,到ui 的destroy时才执行得到
//                    launch {
//                        flowViewModel.wxData.collect { newData ->
//                            Log.d("xfhy666", "getWxData ${newData?.data?.getOrNull(0)?.name}")
//                            tv_data.text = newData?.data?.getOrNull(0)?.name ?: "没获取到数据"
//                        }
//                    }
//                   launch {
//                       flowViewModel.getListData().collect { data ->
//                           Log.d("xfhy666", "getListData 获取到的数据 $data")
//                       }
//                   }
//
//                    launch {
//                        flowViewModel.getThrowFlow().collect {
//                            Log.d("xfhy666", "getThrowFlow 获取到的数据 $it")
//                        }
//                    }

//                    launch {
//                        flowViewModel.errorUseFlow1().collect() {
//                            Log.d("xfhy666", "errorUseFlow1 获取到的数据 $it")
//                        }
//                    }
                }
            }
        }
    }

}