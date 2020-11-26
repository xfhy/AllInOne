package com.xfhy.allinone.opensource.retrofit

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.opensource.retrofit.data.GetRequest
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_retrofit.*

/**
 * Retrofit: 优秀的网络封装框架,内含多种设计模式的灵活运用
 * Retrofit将Http请求抽象成Java接口,采用注解描述和配置网络请求参数.用动态代理动态将该接口的注解翻译成一个Http请求,再通过OKHttp来执行请求.
 *
 * 推荐博客:
 * https://github.com/xfhy/Android-Notes/blob/master/Blogs/Android/%E4%B8%89%E6%96%B9%E5%BA%93%E5%8E%9F%E7%90%86/Retrofit_%E5%8E%9F%E7%90%86%E8%A7%A3%E6%9E%90.md
 * https://www.jianshu.com/p/0c055ad46b6c
 */
class RetrofitActivity : TitleBarActivity() {

    override fun getThisTitle() = "Retrofit Test"

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