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
        //每个请求方法对应着唯一的一个ServiceMethod,因为有缓存的缘故,所以每个请求方法的ServiceMethod只有一个,单例模式

        //5. 采用了装饰模式：ExecutorCallbackCall = 装饰者，而里面真正去执行网络请求的还是OkHttpCall.
        // 使用装饰者模式主要是想在OkHttpCall发送请求时做一些额外操作.这里的额外操作是线程切换,即将子线程切到主线程
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

/*
* 使用动态代理的好处:
* 1. 当NetService对象调用getCall()接口中方法时会进行拦截,调用都会集中转发到InvocationHandler#invoke （）,可集中进行处理
* 2. 获得网络请求接口实例上的所有注解
* 3. 更方便封装ServiceMethod
*
*
*
*
*
*
* Retrofit采用了外观模式统一调用创建网络请求接口实例和网络请求参数配置的方法，具体细节是：
*
* 1. 动态创建网络请求接口的实例（代理模式 - 动态代理）
* 2. 创建 serviceMethod 对象（建造者模式 & 单例模式（缓存机制））
* 3. 对 serviceMethod 对象进行网络请求参数配置：通过解析网络请求接口方法的参数、返回值和注解类型，从Retrofit对象中获取对应的网络请求的url地址、网络请求执行器、网络请求适配器 & 数据转换器。（策略模式）
* 4. 对 serviceMethod 对象加入线程切换的操作，便于接收数据后通过Handler从子线程切换到主线程从而对返回数据结果进行处理（装饰模式）
* 5. 最终创建并返回一个OkHttpCall类型的网络请求对象
*
*
*
*
* 发送请求过程
* 步骤1：对网络请求接口的方法中的每个参数利用对应ParameterHandler进行解析，再根据ServiceMethod对象创建一个OkHttp的Request对象
* 步骤2：使用OkHttp的Request发送网络请求；
* 步骤3：对返回的数据使用之前设置的数据转换器（GsonConverterFactory）解析返回的数据，最终得到一个Response<T>对象
*
* */
