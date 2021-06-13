package com.xfhy.allinone.kotlin.coroutine.retrofit

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_kotlin_coroutine_with_retrofit.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


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
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        lifecycleScope.launch {
            val githubService = retrofit.create(WanAndroidService::class.java)
            val wxList = githubService.listRepos()
            //后台线程请求完之后,再切回来
            tvText.text = wxList?.data?.get(0)?.name ?: "没有获取到"
        }
    }

    private fun initData() {

    }

}

class WxList {
    var errorMsg = ""
    var errorCode = -1
    var data = mutableListOf<Wx>()

    class Wx {
        var id: Int = 0
        var name: String = ""
    }
}

interface WanAndroidService {

    /**
     * 加上suspend之后,Retrofit会将该请求放到后台线程去请求
     */
    @GET("wxarticle/chapters/json")
    suspend fun listRepos(): WxList?
}
