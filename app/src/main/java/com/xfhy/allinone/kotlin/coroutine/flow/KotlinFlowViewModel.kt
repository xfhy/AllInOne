package com.xfhy.allinone.kotlin.coroutine.flow

import androidx.lifecycle.*
import com.xfhy.allinone.App
import com.xfhy.allinone.data.db.AppDatabase
import com.xfhy.allinone.data.db.User
import com.xfhy.allinone.data.net.WANANDROID_BASE_URL
import com.xfhy.allinone.data.net.WanAndroidService
import com.xfhy.allinone.data.net.WxList
import com.xfhy.allinone.util.ApiClient
import com.xfhy.library.ext.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

/**
 * @author : xfhy
 * Create time : 2024/5/29 07:34
 * Description : Flow ViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class KotlinFlowViewModel : ViewModel() {

    private val retrofit = ApiClient.getClient(WANANDROID_BASE_URL)
    private val api = retrofit.create(WanAndroidService::class.java)

    // 网络请求
    // SharingStarted.WhileSubscribed(5000L) : 感觉不合适的方式, 这种方式搞出来的Flow,在Activity中repeatOnLifecycle收集,用户点home键离开页面,在onStop中会把flow取消掉,然后用户重新回到页面时,会重新走网络请求
    // Lazily 是Scope取消的时候才取消,所以这个场景更适合
    val wxData = flow {
        val response = api.listRepos()
        emit(response)
    }.catch {
        log("出错了 $it")
        emit(null)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun fetchData(): Flow<Int> = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }

    // 不太合适的方式
    fun getWxData(): Flow<WxList?> = flow {
        val response = api.listRepos()
        emit(response)
    }.catch {
        log("出错了 $it")
        emit(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    fun getListData(): Flow<Int> = flowOf(1, 2, 3)

    fun errorUseFlow1(): Flow<Int> = flow {
        emit(1)
        // 下面这种是错误的用法
        withContext(Dispatchers.IO) {
            emit(2)
        }
    }

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

    private val userDao = AppDatabase.getDb(App.getAppContext()).userDao()
    private val random = Random(100)

    /**
     * 插入数据到room
     */
    fun insertUserData() {
        val user = User(name = "${random.nextInt()} 罗辑", age = random.nextInt())
//        viewModelScope.launch(Dispatchers.IO) {
//            userDao.insertUser(user)
//        }
        viewModelScope.launch {
            userDao.insertUserBySuspend(user)
        }
    }

    /**
     * collect之后,实时接收数据库中所有的user
     */
    val userDataList: Flow<List<User>?> = userDao
        .getAllBySuspendFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    //-------------LiveData 与 Flow 对比 start ---------
    //-------------1. 基本数据流------------------
    private val _livedata1 = MutableLiveData<String?>()
    val livedata1 = _livedata1
    fun fetchData1() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = api.listRepos()
            _livedata1.postValue(result?.toString())
        }
    }

    val flow1 = flow<String?> {
        val result = api.listRepos()
        emit(result.toString())
    }.flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    //-------------2. switchMap------------------
    private val _liveDataA = MutableLiveData<String>()
    val liveDataB: LiveData<String> = _liveDataA.switchMap { value ->
        MutableLiveData("hh $value")
    }

    fun fetchData2() {
        _liveDataA.value = "param1"
    }

    private val flowA = MutableStateFlow("")
    val flowB: Flow<String> = flowA.flatMapLatest { value ->
        flow {
            emit("hh $value")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    fun fetchFlowA() {
        flowA.value = "param1"
    }

    //-------------3. MediatorLiveData------------------
    private val _liveData31 = MutableLiveData<String>()
    private val _liveData32 = MutableLiveData<Int>()
    val mediatorLiveData: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(_liveData31) { value ->
            value?.let { postValue("liveData31: $it  LiveData32: ${_liveData32.value}") }
        }
        addSource(_liveData32) { value ->
            value?.let { postValue("LiveData32: $it   liveData31: ${_liveData31.value}") }
        }
    }

    fun fetchData3() {
        _liveData31.value = "哈哈"
        _liveData32.value = 6
    }


    private val flow31 = MutableStateFlow("")
    private val flow32 = MutableStateFlow(0)
    val combinedFlow = flow31.combine(flow32) { valueA, valueB ->
        "flow31: $valueA, flow32: $valueB"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    fun fetchData3ByFlow() {
        flow31.value = "哈哈"
        flow32.value = 6
    }

    //-------------4. Transformations.map------------------
    private val liveData4: LiveData<Int> = MutableLiveData()
    val mappedLiveData: LiveData<String> = Transformations.map(liveData4) { value ->
        "Mapped livadata value: $value"
    }

    private val flow4: Flow<Int> = flowOf(1, 2, 3)
    val mappedFlow: Flow<String> = flow4.map { value ->
        "Mapped flow value: $value"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    //-------------5. Transformations.switchMap------------------
    private val _liveData5 = MutableLiveData<String>()
    val switchMappedLiveData: LiveData<String> = Transformations.switchMap(_liveData5) { value ->
        liveData {
            val result = "livedata data $value"
            emit(result)
        }
    }

    fun fetchData5() {
        _liveData5.value = "param1"
    }

    private val flow5 = MutableStateFlow("")
    val switchMappedFlow: Flow<String> = flow5.flatMapLatest { value ->
        flow {
            val result = "flow data $value"
            emit(result)
        }.flowOn(Dispatchers.IO)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    fun fetchFlow5() {
        flow5.value = "param1"
    }

    //-------------LiveData 与 Flow 对比 end ---------

    val stateInFlow = flow {
        emit(1)
        delay(300L)
        emit(2)
        delay(300L)
        emit(3)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val shareInFlow = flow {
        emit(1)
        delay(300L)
        emit(2)
        delay(300L)
        emit(3)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)

    fun testShareIn() {
        viewModelScope.launch {
            launch {
                shareInFlow.collect {
                    log("订阅者1 shareInFlow data $it")
                }
            }
            delay(1000L)
            launch {
                shareInFlow.collect {
                    log("订阅者2 shareInFlow data $it")
                }
            }
        }
    }

}

data class State(val data: Int/*, val timestamp: Long = System.currentTimeMillis()*/)

@OptIn(FlowPreview::class)
fun main(): Unit = runBlocking {
//    val stateFlow = MutableStateFlow(111)
//    stateFlow.emit(111)
//    stateFlow.emit(111)
//    stateFlow.emit(111)
//    stateFlow
//        .collect {
//            println(it)
//        }

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


//    val sharedFlow = MutableSharedFlow<Int>()
//
//    async {
//        sharedFlow.emit(1)
//        sharedFlow.emit(1)
//        sharedFlow.emit(1)
//        sharedFlow.emit(1)
//        sharedFlow.emit(1)
//    }
//
//    sharedFlow.collect {
//        println(it)
//    }

//    suspend fun updateData(value: Int) {
//        stateFlow.emit(State(value))
//    }
//
//    async {
//        updateData(1)
//        delay(100)
//        updateData(1)
//    }
//
//    stateFlow.collect {
//        println(it)
//    }

//    val flow1 = flow {
//        emit(1)
//        delay(100)
//        emit(2)
//        delay(100)
//        emit(3)
//    }
//
//    val flow2 = flow {
//        emit("A")
//        delay(500)
//        emit("B")
//        emit("C")
//    }
//
//    val combinedFlow = flow1.combine(flow2) { a, b -> "$a$b" }
//
//    combinedFlow.collect { println(it) }

//    flowOf(1, 2, 3).zip(flowOf(4, 5, 6)) { a, b ->
//        a + b
//    }.collect {
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
