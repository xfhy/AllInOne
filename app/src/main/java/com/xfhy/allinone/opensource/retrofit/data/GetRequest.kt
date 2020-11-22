package com.xfhy.allinone.opensource.retrofit.data

import com.xfhy.library.ext.logString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : xfhy
 * Create time : 2020/11/22 6:32 PM
 * Description : 实现网络请求
 */
class GetRequest {

    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://fy.iciba.com/")
            //使用Gson解析
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun request() {
        val request = retrofit.create(IGetRequest::class.java)
        val call = request.getCall()
        call.enqueue(object : Callback<Translation> {
            override fun onFailure(call: Call<Translation>?, t: Throwable?) {
                t?.printStackTrace().logString()
            }

            override fun onResponse(call: Call<Translation>?, response: Response<Translation>?) {
                val translation = response?.body()
                translation?.logString()
            }
        })
    }

}