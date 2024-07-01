package com.xfhy.allinone.kotlin.coroutine.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xfhy.allinone.data.WANANDROID_BASE_URL
import com.xfhy.allinone.data.WanAndroidService
import com.xfhy.allinone.data.WxList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : xfhy
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    fun getListData(): Flow<Int> = flowOf(1, 2, 3)

    /**
     * collect此处,   输出:
     * 2024-07-02 07:39:19.803 10024-10024/com.xfhy.allinone D/xfhy666: getThrowFlow 获取到的数据 1
    2024-07-02 07:39:19.803 10024-10024/com.xfhy.allinone D/xfhy666: getThrowFlow 获取到的数据 2

    --------- beginning of crash
    2024-07-02 07:39:19.836 10024-10024/com.xfhy.allinone E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.xfhy.allinone, PID: 10024
    java.lang.RuntimeException: 异常
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowViewModel$getThrowFlow$1.invokeSuspend(KotlinFlowViewModel.kt:47)   // 这一行的ViewModel中我抛出异常的地方
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowViewModel$getThrowFlow$1.invoke(Unknown Source:8)
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowViewModel$getThrowFlow$1.invoke(Unknown Source:4)
    at kotlinx.coroutines.flow.SafeFlow.collectSafely(Builders.kt:61)
    at kotlinx.coroutines.flow.AbstractFlow.collect(Flow.kt:230)    // 到了collect这里
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowActivity$initView$1$1$1$1.invokeSuspend(KotlinFlowActivity.kt:55)
    at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
    at kotlinx.coroutines.EventLoop.processUnconfinedEvent(EventLoop.common.kt:69)
    at kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith(DispatchedContinuation.kt:376)
    at kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable(Cancellable.kt:30)
    at kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable$default(Cancellable.kt:25)
    at kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:110)
    at kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:126)
    at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch(Builders.common.kt:56)
    at kotlinx.coroutines.BuildersKt.launch(Unknown Source:1)
    at kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(Builders.common.kt:47)
    at kotlinx.coroutines.BuildersKt.launch$default(Unknown Source:1)
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowActivity.initView$lambda-0(KotlinFlowActivity.kt:39)    // lifecycleScope.launch
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowActivity.$r8$lambda$l0YhxSN3b9XO48WjJgeEDhKBz1g(Unknown Source:0)
    at com.xfhy.allinone.kotlin.coroutine.flow.KotlinFlowActivity$$ExternalSyntheticLambda0.onClick(Unknown Source:2)
    at android.view.View.performClick(View.java:7448)
    at android.view.View.performClickInternal(View.java:7425)
    at android.view.View.access$3600(View.java:810)
    at android.view.View$PerformClick.run(View.java:28305)
    at android.os.Handler.handleCallback(Handler.java:938)
    at android.os.Handler.dispatchMessage(Handler.java:99)    //  dispatchMessage
    at android.os.Looper.loop(Looper.java:223)
    at android.app.ActivityThread.main(ActivityThread.java:7656)
    at java.lang.reflect.Method.invoke(Native Method)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:592)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:947)
    Suppressed: kotlinx.coroutines.DiagnosticCoroutineContextException: [StandaloneCoroutine{Cancelling}@b417ad2, Dispatchers.Main.immediate]

     */
    fun getThrowFlow(): Flow<Int> = flow {
        emit(1)
        emit(2)
        throw RuntimeException("异常")
    }

}

@OptIn(FlowPreview::class)
fun main(): Unit = runBlocking {
    val stateFlow = MutableStateFlow(111)
    stateFlow.emit(111)
    stateFlow.emit(111)
    stateFlow.emit(111)
    stateFlow
        .collect {
            println(it)
        }

//    flow {
//        emit(1)
//        throw RuntimeException("RuntimeException")
//    }.retry(3).collect {
//        println(it)
//    }

//    flowOf("A","B","C","D","E")
//        .onEach { println("Woman matchmaker emits: $it") }
//        .collect {
//            println("Girl appointment with: $it")
//            delay(1000)
//        }

//    flowOf(1, 2).flatMapConcat { flowOf(it * 2) }.collect {
//        println(it)
//    }


    // 创建3个Flow
//    val firstFlow = flowOf(1, 2)
//    firstFlow.map {
//        it + 2
//    }.collect {
//        println(it)
//    }

//    val secondFlow = flow {
//        emit(3)
//        emit(4)
//    }
//    val thirdFlow = listOf(5, 6).asFlow()
//
//    // 挨个收集
//    firstFlow.collect {
//        println(it)
//    }
//    secondFlow.collect {
//        println(it)
//    }
//    thirdFlow.collect {
//        println(it)
//    }

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

//    val single = flowOf(1, 2, 3).toList()
//    println(single)

//    (1..6).asFlow().transform {
//        emit(it)
//        emit("hhhhh $it")
//        emit(it.toString())
//        emit(it.toLong())
//    }.collect {
//        println(it)
//    }

//    flowOf(1, 2, 3, 4, 5, 6).map {
//        it.toString()
//    }.map {
//        "转换 $it"
//    }.collect {
//        println(it)
//    }

//    flowOf(1, 2, 3, 4, 5, null, 7, 8).filter {
//        it != null
//    }.collect {
//        println(it)
//    }

//    flowOf(1, 2, 3, 4, 5, null, 7, 8).mapNotNull {
//        it?.toString()
//    }.collect {
//        println(it)
//    }

//    val person = Person("一")
//
//    // 结论: 相同的对象,也可以emit多次,然后collect收集到
//    flowOf(person, person, person, person).collect {
//        println(it)
//    }

    // 结论: null也可以emit,然后收集到
//    flow<Any?> {
//        emit(1)
//        emit(null)
//        emit("3")
//    }.collect {
//        println(it)
//    }

    // 结论: zip时,如果2个flow的数据个数不对等,那么谁的个数更少,就仅zip多少个
//    val flow1 = flowOf(1, 2, 3, 4, 5, 6)
//    val flow2 = flowOf("a", "b", "c")
//    flow1.zip(flow2) { value1, value2 ->
//        "新的数据: $value1 $value2"
//    }.collect {
//        println(it)
//    }
    // 输出:
    //新的数据: 1 a
    //新的数据: 2 b
    //新的数据: 3 c

    // take操作符: 我只收集x个,x是take调用时传入的参数,多的不要.  内部是通过抛异常来实现的,但这个异常并不会终止程序执行
//    flowOf(1, 2, 3, 4, 5, 6).take(3).collect {
//        println(it)
//    }

//    val flowstateFlow = flowOf(1, 2, 3, 4, 5)
//    flowstateFlow.collect {
//        println("第一 $it")
//    }
//    flowstateFlow.collect {
//        println("第二 $it")
//    }


}

data class Person(val name: String)
