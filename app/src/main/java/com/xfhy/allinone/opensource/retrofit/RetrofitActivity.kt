package com.xfhy.allinone.opensource.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.opensource.retrofit.data.GetRequest
import kotlinx.android.synthetic.main.activity_retrofit.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Retrofit: 优秀的网络封装框架,内含多种设计模式的灵活运用
 * Retrofit将Http请求抽象成Java接口,采用注解描述和配置网络请求参数.用动态代理动态将该接口的注解翻译成一个Http请求,再通过OKHttp来执行请求.
 */
class RetrofitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        initView()
    }

    private fun initView() {
        btnRetrofitGetRequest.setOnClickListener {
            val request = GetRequest()
            request.request()
        }
    }
}