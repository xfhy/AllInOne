package com.xfhy.allinone.opensource

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.opensource.glide.GlideActivity
import com.xfhy.allinone.opensource.leakcanary.LeakCanaryActivity
import com.xfhy.allinone.opensource.okhttp.OkHttpActivity
import com.xfhy.allinone.opensource.retrofit.RetrofitActivity
import com.xfhy.allinone.opensource.rxjava.RxJavaActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_open_source.*
import org.jetbrains.anko.startActivity

/**
 * 常见开源库源码赏析
 */
class OpenSourceActivity : TitleBarActivity() {

    override fun getThisTitle() = "常见开源库源码赏析"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)

        initView()

    }

    private fun initView() {
        btnGoOkhttp.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnGoRetrofit.setOnClickListener {
            startActivity<RetrofitActivity>()
        }
        btnGoGlide.setOnClickListener {
            startActivity<GlideActivity>()
        }
        btnGoGreenDao.setOnClickListener {
        }
        btnGoRxJava.setOnClickListener {
            startActivity<RxJavaActivity>()
        }
        btnLeakCanary.setOnClickListener {
            startActivity<LeakCanaryActivity>()
        }
        btnGoButterknife.setOnClickListener {
        }
        btnGoEventBus.setOnClickListener {
        }
    }
}