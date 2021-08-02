package com.xfhy.allinone.opensource.rxjava

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.allinone.data.WANANDROID_BASE_URL
import com.xfhy.allinone.data.WanAndroidService
import com.xfhy.allinone.data.WxList
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rxjava.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RxJava
 */
class RxJavaActivity : TitleBarActivity() {

    override fun getThisTitle() = "RxJava Test"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WANANDROID_BASE_URL)
            //使用Gson解析
            .addConverterFactory(GsonConverterFactory.create())
            //转换器   RxJava3   每次执行的时候在IO线程
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)
    }

    fun reqNet(view: View) {
        val request = retrofit.create(WanAndroidService::class.java)
        val call = request.listReposByRxJava()
        call.observeOn(AndroidSchedulers.mainThread()).subscribe(object : SingleObserver<WxList?> {
            override fun onSubscribe(d: Disposable?) {
                disposable = d
                tvContent.text = "开始请求网络"
            }

            override fun onSuccess(t: WxList?) {
                t?.let {
                    tvContent.text = it.data[0].name
                }
            }

            override fun onError(e: Throwable?) {
                tvContent.text = "网络出错"
            }
        })

        //observableMap()
        singleIo()

    }

    private fun observableDelay() {
        Observable.just(1).delay(1, TimeUnit.SECONDS).subscribe(object :Observer<Int> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Int?) {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }

    private fun observableMap() {
        Observable.just(1).map(object : Function<Int, String> {
            override fun apply(t: Int): String {
                return t.toString()
            }
        }).subscribe(object : Observer<String> {
            override fun onNext(t: String?) {
            }

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable?) {
            }

            override fun onError(e: Throwable?) {
            }
        })
    }

    private fun singleIo() {
        val singleInt: Single<Int> = Single.just(1)
        val singleIo = singleInt.subscribeOn(Schedulers.io())
        singleIo.subscribe(object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
                log("onSubscribe")
            }

            override fun onSuccess(t: Int?) {
                log("onSuccess")
            }

            override fun onError(e: Throwable?) {
                log("onError")
            }
        })
    }

    fun single() {
        val just: Single<Int> = Single.just(1)
        just.subscribe(object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onSuccess(t: Int) {
            }

            override fun onError(e: Throwable?) {
            }
        })
    }

    fun singleMap() {
        val singleInt = Single.just(1)
        val singleString = singleInt.map(object : Function<Int, String> {
            override fun apply(t: Int): String {
                return t.toString()
            }
        })
        singleString.subscribe(object : SingleObserver<String> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onSuccess(t: String) {
            }

            override fun onError(e: Throwable?) {
            }
        })
    }

    fun singleDelay() {
        val singleInt: Single<Int> = Single.just(1)
        val singleDelay: Single<Int> = singleInt.delay(1, TimeUnit.SECONDS)
        val observer = object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
                log("onSubscribe")
            }

            override fun onSuccess(t: Int?) {
                log("onSuccess")
            }

            override fun onError(e: Throwable?) {
                log("onError")
            }
        }
        singleDelay.subscribe(observer)
    }

    fun interval() {
        val longObservable: Observable<Long> = Observable.interval(0, 1, TimeUnit.SECONDS)
        longObservable.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Long?) {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}