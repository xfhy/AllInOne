package com.xfhy.allinone.kotlin.coroutine.flow

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.xfhy.allinone.data.WANANDROID_BASE_URL
import com.xfhy.allinone.data.WanAndroidService
import com.xfhy.allinone.data.WxList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : guoliang08
 * Create time : 2024/5/29 07:34
 * Description : Flow ViewModel
 */
class KotlinFlowViewModel : ViewModel() {

    val retrofit = Retrofit.Builder()
        .baseUrl(WANANDROID_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(WanAndroidService::class.java)

    fun fetchData(): Flow<Int> = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }

    // 网络请求,返回将结果返回出去
    fun getWxData(): Flow<WxList?> = flow {
        val response = api.listRepos()
        emit(response)
    }

}

fun main() = runBlocking {
//    val numbersFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
//    val resultFlow: Flow<String> = numbersFlow.filter {
//        println("filter $it")
//        it % 2 == 0
//    }.map { it.toString() }
//
//    resultFlow.collect {
//        println(it)
//    }


    /*
    // 下面这种下发等同于上面那种
    // resultFlow.collect {
    //        println(it)
    //    }

    // 理解: resultFlow是一个Flow对象,而Flow是一个接口,所以调用collect的时候其实接收的是一个FlowCollector对象,
    而FlowCollector其实是只有一个函数的接口,所以能{} 这样写,{}里面就是emit函数的value的值

    public interface Flow<out T> {
        public abstract suspend fun collect(collector: kotlinx.coroutines.flow.FlowCollector<T>): kotlin.Unit
    }

    public fun interface FlowCollector<in T> {
        public abstract suspend fun emit(value: T): kotlin.Unit
    }


    * */
//    resultFlow.collect(object : FlowCollector<String> {
//        override suspend fun emit(value: String) {
//            println(value)
//        }
//    })
//
//    flow {
//        for (i in 1..3) {
//            delay(100)
//            emit(i)
//        }
//    }.collect { value -> println(value) }
//
//    // -------
//    (1..3).asFlow().collect {}
//    flowOf(1, 2, 3).collect {}

//    val job = launch {
//        flow {
//            for (i in 1..3) {
//                delay(1000)
//                if (i == 2) {
//                    // 不能在这里切换线程,会抛异常:   java.lang.IllegalStateException: Flow invariant is violated:
//                    withContext(Dispatchers.IO) {
//                        emit(i)
//                    }
//                } else {
//                    emit(i)
//                }
//            }
//        }.collect {
//            println(it)
//        }
//    }

//    flow {
//        for (i in 1..3) {
//            println("flow  ${currentCoroutineContext()}")
////            delay(100)
//            emit(i)
//        }
//    }.flowOn(Dispatchers.Default)
//        .map {
//            println("map  ${currentCoroutineContext()}")
//            it.toString()
//        }
//        .flowOn(Dispatchers.IO)
//        .collect {
//            withContext(Dispatchers.IO) {
//                println("collect withContext ${currentCoroutineContext()}")
//            }
//            println("collect ${currentCoroutineContext()}")
//            println(it)
//        }

    val single = flowOf(1, 2, 3).toList()
    println(single)
}
