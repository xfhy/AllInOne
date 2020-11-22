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
        //Retrofit本来应该放这里的,最好只有一个实例
        val retrofit = Retrofit.Builder()
            .baseUrl("http://fy.iciba.com/")
            //使用Gson解析
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun request() {

        //方便debug,所以暂时放这里
        //1. 建造者模式: 将一个复杂对象的构建与表示分离,使得用户在不知道对象的创建细节情况下可以直接创建和定制复杂的对象.
        val retrofit = Retrofit.Builder()
            .baseUrl("http://fy.iciba.com/")
            //使用Gson解析
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //4. 通过外观模式和代理模式使用create()方法创建网络请求接口的实例
        //使用外观模式进行访问,里面使用了代理模式
        //外观模式: 定义一个统一接口,外部与通过该统一的接口对子系统里的其他接口进行访问
        //代理模式: 通过访问代理对象的方式来间接访问目标对象
            //静态代理: 代理类在程序运行前已经存在的代理方式
            //动态代理: 代理类在程序运行前不存在,运行时由程序动态生成的代理方式
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

    //2. Retrofit#callFactory 网络请求工厂  工厂模式的体现,将类实例化的操作与使用对象的操作分开,使得使用者不用知道具体参数就可以实例化出所需要的产品类
    //3. Platform#Android#defaultCallbackExecutor() 策略模式

}