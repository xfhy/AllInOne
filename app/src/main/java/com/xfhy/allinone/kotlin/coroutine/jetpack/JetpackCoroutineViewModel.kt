package com.xfhy.allinone.kotlin.coroutine.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.xfhy.library.ext.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author : xfhy
 * Create time : 2021/5/30 6:44 上午
 * Description :
 */
class JetpackCoroutineViewModel : ViewModel() {

    /*

    //老方式使用协程----

    //在这个ViewModel中使用协程时,需要使用这个job来方便控制取消
    private val viewModelJob = SupervisorJob()

    //指定协程在哪里执行,并且可以由viewModelJob很方便地取消uiScope
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun launchDataByOldWay() {
        uiScope.launch {
            //在后台执行
            val result = getNetData()
            //修改UI
            log(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    */

    fun launchData() {
        viewModelScope.launch {
            //在后台执行
            val result = getNetData()
            //修改UI
            log(result)
        }
    }

    //将耗时任务切到IO线程去执行
    private suspend fun getNetData() = withContext(Dispatchers.IO) {
        //模拟网络耗时
        delay(5000)
        //模拟返回结果
        "{}"
    }

    //------------------liveData------------------
    val netData: LiveData<String> = liveData {
        //观察的时候在生命周期内,则会马上执行
        log("执行到liveData里面了")
        val data = getNetData()
        emit(data)
    }

}