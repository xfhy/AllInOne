package com.xfhy.allinone.opensource.rxjava

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.allinone.data.WANANDROID_BASE_URL
import com.xfhy.allinone.data.WanAndroidService
import com.xfhy.allinone.data.WxList
import com.xfhy.library.basekit.activity.TitleBarActivity
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rxjava.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}