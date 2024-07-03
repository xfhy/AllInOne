package com.xfhy.allinone.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * @author : xfhy
 * Create time : 2021/7/25 9:31 上午
 * Description :
 */

const val WANANDROID_BASE_URL = "https://www.wanandroid.com"

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
     * 获取公众号列表
     * 加上suspend之后,Retrofit会将该请求放到后台线程去请求
     */
    @GET("wxarticle/chapters/json")
    suspend fun listRepos(): WxList?

    @GET("wxarticle/chapters/json")
    fun listReposByRxJava(): Single<WxList?>

}
