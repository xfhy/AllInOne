package com.xfhy.allinone.opensource.retrofit.data

import retrofit2.Call
import retrofit2.http.GET

/**
 * @author : xfhy
 * Create time : 2020/11/22 6:30 PM
 * Description : 用于描述网络请求 的接口
 */
interface IGetRequest {

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    fun getCall(): Call<Translation>

}