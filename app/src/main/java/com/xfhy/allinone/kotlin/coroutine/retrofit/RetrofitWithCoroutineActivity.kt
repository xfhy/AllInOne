package com.xfhy.allinone.kotlin.coroutine.retrofit

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.allinone.data.net.WANANDROID_BASE_URL
import com.xfhy.allinone.data.net.WanAndroidService
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_kotlin_coroutine_with_retrofit.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author : xfhy
 * Create time : 2021年06月12日07:41:37
 * Description : Kotlin协程与Retrofit一起使用
 */
class RetrofitWithCoroutineActivity : TitleBarActivity() {

    override fun getThisTitle() = "Kotlin协程与Retrofit一起使用"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_coroutine_with_retrofit)
        initView()
        initData()
    }

    private fun initView() {
        btnReqNet.setOnClickListener {
            reqNet()
        }
    }

    private fun reqNet() {
        val retrofit = Retrofit.Builder()
            .baseUrl(WANANDROID_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(WanAndroidService::class.java)

        /*lifecycleScope.launch {
            val wxList = wanAndroidService.listRepos()
            //后台线程请求完之后,再切回来
            tvText.text = wxList?.data?.get(0)?.name ?: "没有获取到"
        }*/

        lifecycleScope.launch {
            val one = async { api.listRepos() }
            val two = async { api.listRepos() }
            val same = one.await()?.errorCode == two.await()?.errorCode
            tvText.text = "$same same?"
        }
    }

    private fun initData() {

    }

}

